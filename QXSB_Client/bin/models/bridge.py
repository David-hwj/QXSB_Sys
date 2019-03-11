import sys
import cv2
import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
import myModel_bn as Mymodel

'''
这是被前端调用的唯一python文件,需传入一个视频路径作为参数
'''
#video_path='C:/VID20171012101710.mp4'
#video_path='C:/Users/train_video010.avi'

#人脸识别model
face_model='C:/face/cv2/data/haarcascade_frontalface_default.xml'
#face_model = 'C:/Users/sky/AppData/Local/Programs/Python/Python36/Lib/site-packages/cv2/data/haarcascade_frontalface_default.xml'
#人脸大小
IMGSIZE = 96
#str1="0"
#命令行获取视频路径参数
video_path = sys.argv[1]
#cv2读取视频
#f=open('c:/data.txt','w+')
#f.write(str1)
#f.close()
video = cv2.VideoCapture(video_path)
# 获取视频帧数
frameNum = video.get(cv2.CAP_PROP_FRAME_COUNT)
#获取视频帧率
rate = video.get(cv2.CAP_PROP_FPS)
delay = int(1000 / rate)
#加载人脸识别模块
haar = cv2.CascadeClassifier(face_model)

n = 1
#识别出的人脸图片
all_data = []
while 1:
    if (n <= frameNum):
        if (n%100==0):
            print('It has processing %s image.' % n)
        print('It has processing %s image.' % n)
        # 读帧

        success, img = video.read()
        if (success == False):
            print('It has some problem when read video')
            break
        gray_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # 灰度图片
        faces = haar.detectMultiScale(gray_img, 1.1, 5, 1, (100, 100))
        for f_x, f_y, f_w, f_h in faces:  # 把所有脸裁出来
            face = img[f_y:f_y + f_h, f_x:f_x + f_w]
            face = cv2.resize(face, (IMGSIZE, IMGSIZE))
            gray_face = cv2.cvtColor(face, cv2.COLOR_BGR2GRAY)
            data = np.matrix(gray_face)
            all_data.append(data)
        if cv2.waitKey(delay) & 0xFF == ord('q'):
            break
        n+=1
    else:
        break;
video.release()
cv2.destroyAllWindows()
length=len(all_data)

if (length<=0):
    print('no face in video')
else:
    print('there are '+str(length)+' faces in video.')
    pred = Mymodel.getResult(all_data)
    tf.reset_default_graph()
    plt.figure(figsize=(5, 5.5))
    plt.plot(list(range(len(pred))), pred, color='b')
    plt.title("result")
    plt.savefig("C:\\a2\\result.png")
    # plt.show()
    print("qqq")

#f.truncate()
#f=open('c:/data.txt','w+')
#str1="1"
#f.write(str1)
#f.close()
