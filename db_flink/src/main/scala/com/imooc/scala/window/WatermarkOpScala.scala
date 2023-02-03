package com.imooc.scala.window

import java.text.SimpleDateFormat
import java.time.Duration

import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ArrayBuffer
import scala.util.Sorting

/**
 * Watermark+EventTime解决数据乱序问题
 *
 * 在bigdata01上运行nc -l 9001，再启动该程序
 * 输入：
 * 0001,1790820682000
 * 0001,1790820686000
 * 0001,1790820692000
 * 0001,1790820693000
 * 0001,1790820694000
 * 0001,1790820696000
 * 0001,1790820697000
 *
 * Window的触发机制：
 * 先按照自然时间将window划分，如果window是3s，那么1min内会将Window划分为左闭右开的区间
 * 输入的数据根据自身的EventTime，将数据划分到不同的Window中，如果Window中有数据，
 * 则当Watermark时间 >= EventTime时，符合Window触发条件，
 * 最终决定Window触发，是由数据本身的EventTime所属Window中的window_end_time决定。
 * Created by xuwei
 */
object WatermarkOpScala {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //设置使用数据产生的时间：EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //设置全局并行度为1
    env.setParallelism(1)

    //设置自动周期性的产生watermark，默认值为200毫秒
    env.getConfig.setAutoWatermarkInterval(200)

    val text = env.socketTextStream("bigdata01", 9001)
    import org.apache.flink.api.scala._
    //将数据转换为tuple2的形式
    //第一列表示具体的数据，第二列表示是数据产生的时间戳
    val tupStream = text.map(line => {
      val arr = line.split(",")
      (arr(0), arr(1).toLong)
    })

    //分配(提取)时间戳和watermark
    val waterMarkStream = tupStream.assignTimestampsAndWatermarks(
      WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10)) //最大允许的数据乱序时间 10s
        .withTimestampAssigner(new SerializableTimestampAssigner[(String, Long)] {
          val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
          var currentMaxTimestamp = 0L

          //从数据流中抽取时间戳作为EventTime
          override def extractTimestamp(element: (String, Long), recordTimestamp: Long): Long = {
            val timestamp = element._2
            currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp)
            //计算当前watermark，为了打印出来方便观察数据，没有别的作用，watermark=currentMaxTimstamp-OutOfOrderness
            val currentWatermark = currentMaxTimestamp - 10000L
            //此print语句仅仅是为了在学习阶段观察数据的变化
            println("key:" + element._1 + "," +
              "eventTime:[" + element._2 + "|" + sdf.format(element._2) + "]," +
              "currentMaxTimstamp:[" + currentWatermark + "|" + sdf.format(currentMaxTimestamp) + "]," +
              "watermark:[" + currentWatermark + "|" + sdf.format(currentWatermark) + "]")
            element._2
          }
        })
    )

    waterMarkStream.keyBy(0)
      //按照消息的EventTime分配窗口，和调用TimeWindow效果一样
      .window(TumblingEventTimeWindows.of(Time.seconds(3)))
      //使用全量聚合的方式处理window中的数据
      .apply(new WindowFunction[(String, Long), String, Tuple, TimeWindow] {
        override def apply(key: Tuple, window: TimeWindow, input: Iterable[(String, Long)], out: Collector[String]): Unit = {
          val keyStr = key.toString
          //将window中的数据保存到arrBuff中
          val arrBuff = ArrayBuffer[Long]()
          input.foreach(tup => {
            // 存入时间戳
            arrBuff.append(tup._2)
          })
          //将arrBuff转换为arr
          val arr = arrBuff.toArray
          //对arr中的数据进行排序
          Sorting.quickSort(arr)

          val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
          //将目前window内排序后的数据，以及window的开始时间和window的结束时间打印出来，便于观察
          val result = keyStr + "," + arr.length + "," +
            sdf.format(arr.head) + "," + sdf.format(arr.last) + "," +
            sdf.format(window.getStart) + "," + sdf.format(window.getEnd)
          out.collect(result)
        }
      }).print()

    env.execute("WatermarkOpScala")
  }
}
