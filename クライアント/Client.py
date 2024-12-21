import requests
import os

RealUsername = ""
RealPassword = "" #登録やログインで有効と確かめられたユーザーネームとパスワード

ServerIP = input("サーバーURL(例 http://localhost:8080):")
os.system("cls")
try:
    while True:

        print("オンラインメモ帳サービス")
        print("1.register 2.login 3.add_memo 4.get_memos")
        choice = int(input(">>"))
        
        if choice == 1:
            username=input("ユーザーネーム:")
            password=input("パスワード:")
            r=requests.get(f"{ServerIP}/register?username={username}&password={password}")
            if r.status_code == 200:
                RealUsername = username
                RealPassword = password

                
            input(r.text+" :")
            os.system("cls")
            
            
        elif choice == 2:
            username=input("ユーザーネーム:")
            password=input("パスワード:")
            r=requests.get(f"{ServerIP}/login?username={username}&password={password}")
            if r.status_code == 200:
                RealUsername = username
                RealPassword = password
            input(r.text+" :")
            os.system("cls")
            
                
        elif choice == 3:
            if RealUsername != "" and RealPassword != "":
                memo = input("追加するメモ:")
                r=requests.get(f"{ServerIP}/add_memo?username={RealUsername}&password={RealPassword}&memo={memo}")
                input(r.text + " :")
                os.system("cls")
                
            else:
                input("先に登録もしくはログインしてください。 :")
                os.system("cls")
                
        
        elif choice == 4:
            if RealUsername != "" and RealPassword != "":
                r=requests.get(f"{ServerIP}/get_memos?username={RealUsername}&password={RealPassword}")
                if r.status_code == 200:
                    memos = r.text.split("&")
                    
                    for index,memo in enumerate(memos):
                        print(f"{index+1}個目のメモ:")
                        print(memo)
                    input("以上 :")
                    os.system("cls")
                    
                else:
                    input(r.text+" :")
                    os.system("cls")
                     
            else:
                input("先に登録もしくはログインしてください。 :")
                os.system("cls")
        else:
            os.system("cls")
                
except Exception as e:
    input(e)
        