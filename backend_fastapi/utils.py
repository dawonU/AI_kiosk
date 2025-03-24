import os
import mysql.connector

def mysql_connect():
    con = mysql.connector.connect(
        host="127.0.0.1",
        port=3306,
        user="root",
        password="",
        database="Data"
    )
    cur = con.cursor(dictionary=True)
    return con, cur