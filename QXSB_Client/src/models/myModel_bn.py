#coding=utf-8

import tensorflow as tf
import gc
import numpy as np
import matplotlib.pyplot as plt
from tensorflow.contrib.layers.python.layers import batch_norm
import os

#定义常量
rnn_unit = 64          #rnn隐藏单元数
num_layers = 1            #双向rnn层数
input_size = 96     #输入图片大小
input_channels = 1   #输入通道数
seq_length_each_batch = 32   #每个样本的序列长度
time_step = 8

def getData(alldata,start,end):     # 数据集文件名，从start到end张
    totalNum = len(alldata)
    if end>totalNum:
        print('there is no so much dataset!!!')
        return

    data=[]
    alldata = np.reshape(alldata,[-1,96,96],order='C')
    for i in range(start,end):
        data.append(alldata[i])
    data = np.reshape(data, (-1, 96, 96), order='C')
    return data


def getDataNum(alldata):#数据集文件名
    totalNum = len(alldata)  # 获取数据集大小
    return totalNum


def batch_norm_layer(value,is_training=False,name='batch_norm'):
    if is_training is True:
        # 训练模式 使用指数加权函数不断更新均值和方差
        return tf.contrib.layers.batch_norm(inputs=value, decay=0.9, updates_collections=None, is_training=True)
    else:
        # 测试模式 不更新均值和方差，直接使用
        return tf.contrib.layers.batch_norm(inputs=value, decay=0.9, updates_collections=None, is_training=False)


def weight_init(shape, name):
    '''
    获取某个shape大小的参数
    '''
    return tf.get_variable(name, shape, initializer=tf.contrib.layers.variance_scaling_initializer(factor=1, mode='FAN_IN'))


def bias_init(shape, name):
    return tf.get_variable(name, shape, initializer=tf.constant_initializer(0.0))


def conv2d(x,conv_w):
    return tf.nn.conv2d(x, conv_w, strides=[1, 1, 1, 1], padding='VALID')


def conv2d_s(x,conv_w):
    return tf.nn.conv2d(x, conv_w, strides=[1, 1, 1, 1], padding='SAME')


def max_pool(x, size):
    return tf.nn.max_pool(x, ksize=[1,size,size,1], strides = [1,size,size,1], padding='VALID')


# 获取测试数据集
def getTest(data_num,data_start = 4000,data_end = 4800):
    data_x = getData(data_num,data_start,data_end)
    data_x = np.asarray(data_x, dtype=np.double)
    data_x = data_x *(1.0/255) - 0.5
    image = []
    size = (len(data_x) - time_step) // seq_length_each_batch
    if size > 0:
        temp_x = data_x[0:seq_length_each_batch + time_step, :, :]
        image.append(temp_x)
        for i in range(1, size):
            temp_x = data_x[i * seq_length_each_batch - time_step:(i + 1) * seq_length_each_batch, :, :]
            image.append(temp_x)
        rest = (len(data_x) - time_step) % seq_length_each_batch
        if rest > 0:
            temp_x = data_x[0 - rest - time_step:, :, :]
            last = temp_x[-1:, :, :]
            for i in range(rest, seq_length_each_batch):
                temp_x = np.concatenate([temp_x, last], axis=0)
            image.append(temp_x)
            size = size + 1
    else:
        rest = len(data_x)
        temp_x = data_x
        last = temp_x[-1:, :, :]
        for i in range(rest, seq_length_each_batch + time_step):
            temp_x = np.concatenate([temp_x, last],axis=0)
        image.append(temp_x)
        size = size + 1
    image = np.asarray(image)
    image = np.reshape(image, [size, seq_length_each_batch+time_step, 96, 96, 1])
    return size, image, rest


def inference(input_data,is_training):
    with tf.name_scope('conv0'):
        w_conv0 = weight_init([3, 3, 1, 64], 'conv0_w')
        b_conv0 = bias_init([64], 'conv0_b')

        # 卷积之后图片大小变成96
        h_conv0 = tf.nn.relu(conv2d_s(input_data, w_conv0) + b_conv0)

    with tf.name_scope('conv1'):
        w_conv1 = weight_init([3, 3, 64, 64], 'conv1_w')
        b_conv1 = bias_init([64], 'conv1_b')

        # 卷积之后图片大小变成96
        h_conv1 = tf.nn.relu(conv2d_s(h_conv0, w_conv1) + b_conv1)
        # 池化之后图片大小变成48
        h_pool1 = max_pool(h_conv1, 2)

    with tf.name_scope('conv2'):
        w_conv2 = weight_init([3, 3, 64, 128], 'conv2_w')
        b_conv2 = bias_init([128], 'conv2_b')

        # 卷积之后图片大小变成 48
        # h_conv2 = tf.nn.relu(conv2d_s(h_pool1, w_conv2) + b_conv2)
        h_conv2 = tf.nn.relu(batch_norm_layer(conv2d_s(h_pool1, w_conv2) + b_conv2, is_training=is_training))

    with tf.name_scope('conv3'):
        w_conv3 = weight_init([3, 3, 128, 128], 'conv3_w')
        b_conv3 = bias_init([128], 'conv3_b')

        # 卷积之后图片大小变成48
        h_conv3 = tf.nn.relu(conv2d_s(h_conv2, w_conv3) + b_conv3)
        # 池化之后图片大小变成24
        h_pool3 = max_pool(h_conv3, 2)

    with tf.name_scope('conv4'):
        w_conv4 = weight_init([3, 3, 128, 256], 'conv4_w')
        b_conv4 = bias_init([256], 'conv4_b')

        # 卷积之后图片大小变成24
        h_conv4 = tf.nn.relu(conv2d_s(h_pool3, w_conv4) + b_conv4)

    with tf.name_scope('conv5'):
        w_conv5 = weight_init([3, 3, 256, 256], 'conv5_w')
        b_conv5 = bias_init([256], 'conv5_b')

        # 卷积之后图片大小变成24
        h_conv5 = tf.nn.relu(conv2d_s(h_conv4, w_conv5) + b_conv5)

    with tf.name_scope('conv6'):
        w_conv6 = weight_init([3, 3, 256, 256], 'conv6_w')
        b_conv6 = bias_init([256], 'conv6_b')

        # 卷积之后图片大小变成24
        h_conv6 = tf.nn.relu(conv2d_s(h_conv5, w_conv6) + b_conv6)
        # 池化之后图片大小变成12
        h_pool6 = max_pool(h_conv6, 2)

    with tf.name_scope('conv7'):
        w_conv7 = weight_init([3, 3, 256, 256], 'conv7_w')
        b_conv7 = bias_init([256], 'conv7_b')

        # 卷积之后图片大小变成12
        # h_conv7 = tf.nn.relu(conv2d_s(h_pool6, w_conv7) + b_conv7)
        h_conv7 = tf.nn.relu(batch_norm_layer(conv2d_s(h_pool6, w_conv7) + b_conv7, is_training=is_training))

    with tf.name_scope('conv8'):
        w_conv8 = weight_init([3, 3, 256, 256], 'conv8_w')
        b_conv8 = bias_init([256], 'conv8_b')

        # 卷积之后图片大小变成12
        h_conv8 = tf.nn.relu(conv2d_s(h_conv7, w_conv8) + b_conv8)

    with tf.name_scope('conv9'):
        w_conv9 = weight_init([3, 3, 256, 256], 'conv9_w')
        b_conv9 = bias_init([256], 'conv9_b')

        # 卷积之后图片大小变成12
        h_conv9 = tf.nn.relu(conv2d_s(h_conv8, w_conv9) + b_conv9)
        # 池化之后图片大小变成6
        h_pool9 = max_pool(h_conv9, 2)

    with tf.name_scope('conv10'):
        w_conv10 = weight_init([3, 3, 256, 512], 'conv10_w')
        b_conv10 = bias_init([512], 'conv10_b')

        # 卷积之后图片大小变成6
        h_conv10 = tf.nn.relu(conv2d_s(h_pool9, w_conv10) + b_conv10)

    with tf.name_scope('conv11'):
        w_conv11 = weight_init([3, 3, 512, 512], 'conv11_w')
        b_conv11 = bias_init([512], 'conv11_b')

        # 卷积之后图片大小变成6
        h_conv11 = tf.nn.relu(conv2d_s(h_conv10, w_conv11) + b_conv11)

    with tf.name_scope('conv12'):
        w_conv12 = weight_init([3, 3, 512, 512], 'conv12_w')
        b_conv12 = bias_init([512], 'conv12_b')

        # 卷积之后图片大小变成6
        h_conv12 = tf.nn.relu(conv2d_s(h_conv11, w_conv12) + b_conv12)
        # 池化之后图片大小变成3
        h_pool12 = max_pool(h_conv12, 2)

    with tf.name_scope('fc1'):
        w_fc1 = weight_init([3*3*512, 128], 'fc1_w')
        b_fc1 = bias_init([128], 'fc1_b')

        h_fc1 = tf.nn.relu(tf.matmul(tf.reshape(h_pool12, [-1, 3*3*512]), w_fc1) + b_fc1)

    with tf.name_scope('fc2'):
        w_fc2 = weight_init([128, 128], 'fc2_w')
        b_fc2 = bias_init([128], 'fc2_b')

        h_fc2 = tf.matmul(h_fc1, w_fc2) + b_fc2
        cnn_output = tf.nn.relu(tf.reshape(h_fc2,[-1, 128]))
        tf.summary.histogram("w_fc2", w_fc2)

    with tf.name_scope('reshape'):
        seq_temp = []
        lstm_input = tf.slice(cnn_output, [0, 0], [time_step, 128])
        lstm_input = tf.reshape(lstm_input,[1,-1,128])
        seq_temp.extend([time_step])
        count = np.shape(cnn_output)[0]-time_step
        for i in range(1, count):
            temp = tf.reshape(tf.slice(cnn_output, [i, 0], [time_step, 128]),[1,-1,128])
            lstm_input = tf.concat([lstm_input, temp], axis=0)
            seq_temp.extend([time_step])

    with tf.name_scope('lstm'):
        cell_fw = tf.contrib.rnn.LSTMCell(rnn_unit, forget_bias=1.0,
                                          initializer=tf.random_normal_initializer(
                                              mean=0.0, stddev=0.1),
                                          state_is_tuple=True)

        cells_fw = [cell_fw] * num_layers
        # 定义一个向后计算的LSTM单元
        cell_bw = tf.contrib.rnn.LSTMCell(rnn_unit, forget_bias=1.0,
                                          initializer=tf.random_normal_initializer(
                                              mean=0.0, stddev=0.1),
                                          state_is_tuple=True)
        # 组成一个有2个cell的list
        cells_bw = [cell_bw] * num_layers

        # 将前面定义向前计算和向后计算的2个cell的list组成双向lstm网络
        outputs, _, _ = tf.contrib.rnn.stack_bidirectional_dynamic_rnn(cells_fw,
                                                                       cells_bw,
                                                                       lstm_input,
                                                                       dtype=tf.float32,
                                                                       sequence_length=seq_temp)
        output_lstm = tf.slice(outputs,[0, time_step - 1, 0], [count, 1, rnn_unit*2])
        output_lstm = tf.reshape(output_lstm,[-1, rnn_unit*2])

    with tf.name_scope('fc3'):
        w_fc3 = weight_init([rnn_unit*2, rnn_unit], 'fc3_w')
        b_fc3 = bias_init([rnn_unit], 'fc3_b')

        h_fc3 = tf.nn.tanh(tf.matmul(output_lstm, w_fc3) + b_fc3)

    with tf.name_scope('fc4'):
        w_fc4 = weight_init([rnn_unit, 1], 'fc4_w')
        b_fc4 = bias_init([1], 'fc4_b')

        final_output = tf.matmul(h_fc3, w_fc4) + b_fc4
        return final_output


'''
    with tf.name_scope('fc3'):
        w_fc3 = weight_init([128, 128], 'fc3_w')
        b_fc3 = bias_init([128], 'fc3_b')

        h_fc3 = tf.nn.tanh(tf.matmul(cnn_output, w_fc3) + b_fc3)
        tf.summary.histogram("w_fc3", w_fc3)
        if flag == 1:
            h_fc3 = tf.nn.dropout(h_fc3, drop_rate)

    with tf.name_scope('fc4'):
        w_fc4 = weight_init([128, 1], 'fc4_w')
        b_fc4 = bias_init([1], 'fc4_b')

        final_output = tf.matmul(h_fc3, w_fc4) + b_fc4
    return final_output
'''


#模型预测
def predition(data_num,test_begin = 4000,test_end = 4800):
    inputs = tf.placeholder(tf.float32, shape=[seq_length_each_batch+time_step, input_size, input_size, input_channels])
    is_training = tf.placeholder(dtype=tf.bool)
    with tf.variable_scope('inference',reuse=tf.AUTO_REUSE):
        result = inference(inputs, is_training)
    var_list = tf.trainable_variables()
    g_list = tf.global_variables()
    temp_vars = [g for g in g_list if 'moving_mean' in g.name]
    temp_vars += [g for g in g_list if 'moving_variance' in g.name]
    var_list += temp_vars
    saver = tf.train.Saver(var_list=var_list, max_to_keep=1)
    module_file = tf.train.latest_checkpoint("C:/face/model1/")
    with tf.Session() as sess:
        size, test_x, rest = getTest(data_num, test_begin, test_end)
        sess.run(tf.global_variables_initializer())
        sess.run(tf.local_variables_initializer())
        saver.restore(sess, module_file)
        pred_list = []
        for i in range(size):
            feed = {inputs: test_x[i],
                    is_training: False}
            pred_list_temp = sess.run(result, feed_dict=feed)
            pred_list.extend(np.reshape(pred_list_temp,[-1]))
        pred_list = np.reshape(pred_list,[-1],order='C')
        pred_list = pred_list[0:len(data_num)]
        return pred_list

def getResult(data):
    n = int(getDataNum(data))
    count = int(n / 2000)
    pred = []
    temp_pred = []
    for i in range(count):
        if n < 2000:
            break;
        temp_pred = predition(data,count*2000,(count*2000+1999))
        pred.extend(temp_pred)
    temp_pred = predition(data,count*n,n)
    pred.extend(temp_pred)
    return pred
