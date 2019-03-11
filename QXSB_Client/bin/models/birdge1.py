import sys
import cv2
import numpy as np
import myModel_cnn as Mymodel
import matplotlib.pyplot as plt
import os

'''
杩欐槸琚墠绔皟鐢ㄧ殑鍞竴python鏂囦欢,闇�浼犲叆涓�涓棰戣矾寰勪綔涓哄弬鏁�
'''
image_path='C:\\a1\\'


#浜鸿劯璇嗗埆model
face_model='C:\\face\\cv2\\data\\haarcascade_frontalface_default.xml'
#face_model = 'C:/Users/sky/AppData/Local/Programs/Python/Python36/Lib/site-packages/cv2/data/haarcascade_frontalface_default.xml'
#浜鸿劯澶у皬
IMGSIZE = 96

#鍛戒护琛岃幏鍙栬棰戣矾寰勫弬鏁�
#video_path = sys.argv[1]
#cv2璇诲彇瑙嗛
# 鑾峰彇瑙嗛甯ф暟
#鑾峰彇瑙嗛甯х巼
#鍔犺浇浜鸿劯璇嗗埆妯″潡
haar = cv2.CascadeClassifier(face_model)

n = 0
#璇嗗埆鍑虹殑浜鸿劯鍥剧墖
all_data = []
while 1:
    path = image_path+str(n)+".png"
    if os.path.exists(path) == False:
        break
    n = n + 1
    img = cv2.imread(path)
    # os.remove(path)
    gray_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # 鐏板害鍥剧墖
    faces = haar.detectMultiScale(gray_img,  1.1, 5, 1, (100, 100))
    for f_x, f_y, f_w, f_h in faces:  # 鎶婃墍鏈夎劯瑁佸嚭鏉�
        face = img[f_y:f_y + f_h, f_x:f_x + f_w]
        face = cv2.resize(face, (IMGSIZE, IMGSIZE))
        gray_face = cv2.cvtColor(face, cv2.COLOR_BGR2GRAY)
        data = np.matrix(gray_face)
        all_data.append(data)
cv2.destroyAllWindows()
length=len(all_data)

if (length<=0):
    print('no face in video')
else:
    print('there are '+str(length)+' faces in video.')
    pred = Mymodel.getResult(all_data)
    plt.figure(figsize=(5, 5))
    plt.bar(range(3), pred, color='b')
    plt.title("result")
    plt.savefig("C:\\a2\\result.png")
    # plt.show()
    print("qqq")
