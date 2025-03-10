import datetime
from typing import Union
from fastapi import FastAPI
from model import SaleItem
from utils import mysql_connect

app = FastAPI()

# 단일 메뉴
@app.get("/menu")
def get_detail_menu(menu_id: int, category_id: int):
    con, cur = mysql_connect()
    sql = """select * from menu where menu_id={} AND category_id={};""".format(menu_id, category_id)
    cur.execute(sql)
    result = cur.fetchall()
    print(result)
    cur.close()
    con.close()
    return result

# 전체 메뉴 
@app.get("/menus")
def get_menu():
    con, cur = mysql_connect()
    sql = """select * from menu;"""
    cur.execute(sql)
    result = cur.fetchall()
    # print(result)
    cur.close()
    con.close()
    return result

@app.post("/sales")
def sales(item: SaleItem):
    item_dict = item.model_dump()
    ddnow = datetime.datetime.now()
    for item in item_dict['data']:
        item['sales_at'] = ddnow
        print(item)
    con, cur = mysql_connect()
    sql = """INSERT INTO kiosk.sales (menu_id, qty, price, sales_at) VALUES (%(menu_id)s, %(qty)s, %(price)s, %(sales_at)s);"""
    cur.executemany(sql, item_dict['data'])
    con.commit()
    result = cur.fetchall()
    # print(result)
    cur.close()
    con.close()
    return item

