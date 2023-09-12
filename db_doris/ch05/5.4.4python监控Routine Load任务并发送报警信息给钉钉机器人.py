#!/usr/bin/python
# -*- coding: UTF-8 -*-
import pymysql 
import requests
import sys 
if sys.getdefaultencoding() != 'utf-8':
    reload(sys)
    sys.setdefaultencoding('utf-8')
SEND_URL=dingdingAPI
def send_message(mess):
    print("收到的消息内如如图：%s"%mess)
    pagrem = {
        "msgtype": "text",
        "text": {
            "content": mess,
            "mentioned_list":["@all"],
            "mentioned_mobile_list":["@all"] 
        }, 
    }
    headers = {
        'Content-Type': 'application/json'
    }
    print(json.dumps(pagrem))
    resp = requests.post(SEND_URL, data=json.dumps(pagrem), headers=headers)
    print(resp.status_code)
    print(resp.text)
    
def check_schema_load(schema):
    # 连接database
    conn = pymysql.connect(
        host='192.168.x.x',
        port=9030,
        user='root',
        password='xxxx',
        database=schema,
        charset='utf8')
    # 得到一个可以执行SQL语句的光标对象
    cursor = conn.cursor()  # 执行完毕返回的结果集默认以元组显示
    # 得到一个可以执行SQL语句并且将结果作为字典返回的游标
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)
    
    # 定义要执行的SQL语句
    sql = """
    show routine load;
    """
    
    # 执行SQL语句
    cursor.execute(sql)
    res = cursor.fetchall()
    err_list=[]
    print(u"查询到DRP的routine load任务数为：%d"%len(res))
    for row in res:
        print(u"任务ID：%s   任务名：%s  目标数据库：%s  目标表名：%s  任务状态：%s"%(row['Id'],row['Name'],row['DbName'],row['TableName'],row['State']))
        if (row['State'] == "RUNNING"):
            continue
        else:
            row_mess = u"任务名称：%s 目标表名：%s   状态变化原因：%s  错误日志URL：%s  补充信息：%s "%(row['Name'],row['TableName'],row['ReasonOfStateChanged'],row['ErrorLogUrls'],row['OtherMsg'])
            err_list.append(row_mess)
    
    err_mess =u'检查到%s模式下的异常routine load有：\n'%schema
    if len(err_list) >0 :
        for index,mess in enumerate(err_list): 
            print(mess)
            err_mess = err_mess + str(index+1) + '. ' +  mess + '\n'
        send_message(err_mess)       
   
    # 关闭光标对象
    cursor.close()
    # 关闭数据库连接
    conn.close()
if __name__ == "__main__":
    schema_list = ['ods_drp','ods_hana','ods_e3_fx','ods_e3_jv','ods_e3_zy','ods_e3_pld','ods_rpa','ods_xgs','ods_xt1']
    for schema in schema_list:
        check_schema_load(schema)