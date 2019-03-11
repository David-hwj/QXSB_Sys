#coding=utf-8

import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt

#定义常量
rnn_unit = 40          #rnn隐藏单元数
num_layers = 1            #双向rnn层数
input_size = 96     #输入图片大小
input_channels = 1   #输入通道数
#data_num = 1         #选择数据集
seq_length_each_batch = 16   #每个样本的序列长度
learning_rate = 0.0000001

def weight_init(shape, name):
    '''
    获取某个shape大小的参数
    '''
    return tf.get_variable(name, shape, initializer=tf.random_normal_initializer(mean=0.0, stddev=0.1))

def bias_init(shape, name):
    return tf.get_variable(name, shape, initializer=tf.constant_initializer(0.0))

def conv2d(x,conv_w):
    return tf.nn.conv2d(x, conv_w, strides=[1, 1, 1, 1], padding='VALID')

def conv2d_s(x,conv_w):
    return tf.nn.conv2d(x, conv_w, strides=[1, 1, 1, 1], padding='SAME')

def max_pool(x, size):
    return tf.nn.max_pool(x, ksize=[1,size,size,1], strides = [1,size,size,1], padding='VALID')


def getData(alldata,start,end):#数据集文件名，从start到end张
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


#获取测试数据集
def getTest(alldata,data_start = 4000,data_end = 4800):
    data_x = getData(alldata,data_start,data_end)
    data_x = np.asarray(data_x)
    data_x = data_x *(1.0/255) - 0.5
    size = (len(data_x) + seq_length_each_batch - 1) // seq_length_each_batch
    image = []
    sample_size = []
    for i in range(size - 1):
        temp_x = data_x[i*seq_length_each_batch:(i+1)*seq_length_each_batch,:,:]
        temp_x = np.reshape(temp_x,[-1,96,96,1],order='C')
        image.append(temp_x)
        sample_size.append(seq_length_each_batch)
    temp_x = data_x[(size - 1) * seq_length_each_batch:, :, :]
    temp_x = np.reshape(temp_x, [-1, 96, 96, 1], order='C')
    image.append(temp_x)
    if len(data_x)%seq_length_each_batch != 0:
        sample_size.append(len(data_x)%seq_length_each_batch)
    else:
        sample_size.append(seq_length_each_batch)
    image = np.asarray(image)
    return size, image, sample_size

def inference(input_data,seq_len):
    with tf.name_scope('conv0'):
        w_conv0 = weight_init([3, 3, 1, 32], 'conv0_w')
        b_conv0 = bias_init([32], 'conv0_b')

        # 卷积之后图片大小变成96
        h_conv0 = tf.nn.leaky_relu(conv2d_s(input_data, w_conv0) + b_conv0)

    with tf.name_scope('conv1'):
        w_conv1 = weight_init([5, 5, 32, 32], 'conv1_w')
        b_conv1 = bias_init([32], 'conv1_b')

        # 卷积之后图片大小变成92
        h_conv1 = tf.nn.leaky_relu(conv2d(h_conv0, w_conv1) + b_conv1)
        # 池化之后图片大小变成46
        h_pool1 = max_pool(h_conv1, 2)

    with tf.name_scope('conv2'):
        w_conv2 = weight_init([3, 3, 32, 32], 'conv2_w')
        b_conv2 = bias_init([32], 'conv2_b')

        # 卷积之后图片大小变成 46
        h_conv2 = tf.nn.leaky_relu(conv2d_s(h_pool1, w_conv2) + b_conv2)

    with tf.name_scope('conv3'):
        w_conv3 = weight_init([3, 3, 32, 32], 'conv3_w')
        b_conv3 = bias_init([32], 'conv3_b')

        # 卷积之后图片大小变成46
        h_conv3 = tf.nn.leaky_relu(conv2d_s(h_conv2, w_conv3) + b_conv3)

    with tf.name_scope('conv4'):
        w_conv4 = weight_init([3, 3, 32, 32], 'conv4_w')
        b_conv4 = bias_init([32], 'conv4_b')

        # 卷积之后图片大小变成46
        h_conv4 = tf.nn.leaky_relu(conv2d_s(h_conv3, w_conv4) + b_conv4)
        # 池化之后图片大小变成23
        h_pool4 = max_pool(h_conv4, 2)

    with tf.name_scope('conv5'):
        w_conv5 = weight_init([3, 3, 32, 32], 'conv5_w')
        b_conv5 = bias_init([32], 'conv5_b')

        # 卷积之后图片大小变成23
        h_conv5 = tf.nn.leaky_relu(conv2d_s(h_pool4, w_conv5) + b_conv5)

    with tf.name_scope('conv6'):
        w_conv6 = weight_init([3, 3, 32, 32], 'conv6_w')
        b_conv6 = bias_init([32], 'conv6_b')

        # 卷积之后图片大小变成23
        h_conv6 = tf.nn.leaky_relu(conv2d_s(h_conv5, w_conv6) + b_conv6)

    with tf.name_scope('conv7'):
        w_conv7 = weight_init([3, 3, 32, 32], 'conv7_w')
        b_conv7 = bias_init([32], 'conv7_b')

        # 卷积之后图片大小变成23
        h_conv7 = tf.nn.leaky_relu(conv2d_s(h_conv6, w_conv7) + b_conv7)
        # 池化之后图片大小变成11
        h_pool7 = max_pool(h_conv7, 2)

    with tf.name_scope('conv8'):
        w_conv8 = weight_init([3, 3, 32, 32], 'conv8_w')
        b_conv8 = bias_init([32], 'conv8_b')

        # 卷积之后图片大小变成11
        h_conv8 = tf.nn.leaky_relu(conv2d_s(h_pool7, w_conv8) + b_conv8)
        # 池化之后图片大小变成5
        h_pool8 = max_pool(h_conv8, 2)

    with tf.name_scope('fc1'):
        w_fc1 = weight_init([5*5*32, 128], 'fc1_w')
        b_fc1 = bias_init([128], 'fc1_b')

        h_fc1 = tf.nn.leaky_relu(tf.matmul(tf.reshape(h_pool8, [-1, 5*5*32]), w_fc1) + b_fc1)

    #keep_prob = 0.8          #弃权技术
    #h_fc1 = tf.nn.dropout(h_fc1,keep_prob)
    with tf.name_scope('fc2'):
        w_fc2 = weight_init([128, 128], 'fc2_w')
        b_fc2 = bias_init([128], 'fc2_b')

        h_fc2 = tf.matmul(h_fc1, w_fc2) + b_fc2
        cnn_output = tf.reshape(h_fc2,[-1,seq_len[0],128])

    with tf.name_scope('lstm'):
        cell_fw = tf.contrib.rnn.LSTMCell(rnn_unit,
                                          initializer=tf.random_normal_initializer(
                                              mean=0.0, stddev=0.1),
                                          state_is_tuple=True)

        cells_fw = [cell_fw] * num_layers
        # 定义一个向后计算的LSTM单元，40个隐藏单元
        cell_bw = tf.contrib.rnn.LSTMCell(rnn_unit,
                                          initializer=tf.random_normal_initializer(
                                              mean=0.0, stddev=0.1),
                                          state_is_tuple=True)
        # 组成一个有2个cell的list
        cells_bw = [cell_bw] * num_layers

        # 将前面定义向前计算和向后计算的2个cell的list组成双向lstm网络
        outputs, _, _ = tf.contrib.rnn.stack_bidirectional_dynamic_rnn(cells_fw,
                                                                       cells_bw,
                                                                       cnn_output,
                                                                       dtype=tf.float32,
                                                                       sequence_length=seq_len)
        output_lstm = tf.reshape(outputs,[-1,rnn_unit*2])

    with tf.name_scope('fc3'):
        w_fc3 = weight_init([rnn_unit*2, rnn_unit], 'fc3_w')
        b_fc3 = bias_init([rnn_unit], 'fc3_b')

        h_fc3 = tf.nn.leaky_relu(tf.matmul(output_lstm, w_fc3) + b_fc3)

    #h_fc3 = tf.nn.dropout(h_fc3, keep_prob)

    with tf.name_scope('fc4'):
        w_fc4 = weight_init([rnn_unit, 1], 'fc4_w')
        b_fc4 = bias_init([1], 'fc4_b')

        final_output = tf.matmul(h_fc3, w_fc4) + b_fc4

    return final_output


#模型预测
def predition(data,test_begin = 4000,test_end = 4800):
    inputs = tf.placeholder(tf.float32, shape=[None, input_size,input_size,input_channels])
    seq_len = tf.placeholder(tf.int32, shape=[None])
    with tf.variable_scope('inference'):
        result = inference(inputs, seq_len)
    saver = tf.train.Saver(tf.global_variables())
    with tf.Session() as sess:
        size, test_x,sample_size= getTest(data, test_begin, test_end)
        module_file = tf.train.latest_checkpoint("C:/face/model1/")
        saver.restore(sess, module_file)
        pred_list = []
        for i in range(size):
            feed = {inputs: test_x[i],
                    seq_len: [sample_size[i]]}
            pred_list_temp = sess.run(result, feed_dict=feed)
            pred_list.extend(np.reshape(pred_list_temp,[-1]))
        pred_list = np.reshape(pred_list,[-1],order='C')
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
    plt.figure(figsize=(5, 5.5))
    plt.plot(list(range(len(pred))), pred, color='b')
    plt.title("result")
    plt.savefig("C:\\a2\\result.png")
    #plt.show()
    print("qqq")
