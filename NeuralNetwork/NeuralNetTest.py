#!/usr/bin/env python
# coding: utf-8

# In[1]:


import numpy
import os
from imread import imread, imsave
from keras.datasets import mnist
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import Flatten
from keras.layers.convolutional import Conv2D
from keras.layers.convolutional import MaxPooling2D
from keras.utils import np_utils
from keras import backend as k
from keras.models import model_from_json
import matplotlib.pyplot as plt

#fixing a random seed
seed = 7
numpy.random.seed(seed)

#loading the data
(trainData_x, trainData_y), (testData_x, testData_y) = mnist.load_data()

#Reshae the dataset 
trainData_x  = trainData_x.reshape(trainData_x.shape[0],1,28,28).astype('float32')
testData_x  = testData_x.reshape(testData_x.shape[0],1,28,28).astype('float32')

#normalize the input data from 255 to 0-1
trainData_x = trainData_x/255
testData_x = testData_x/255

#encode the outputs
trainData_y = np_utils.to_categorical(trainData_y)
testData_y = np_utils.to_categorical(testData_y)
numberOfClases = testData_y.shape[1]
#----------------------------------------------------------------------->


#load model of NN
json_file = open('NeuralNetwork\\model.json','r')
NNLoadedModel = json_file.read()
json_file.close()
NNModel = model_from_json(NNLoadedModel)

#loading the weights
NNModel.load_weights('NeuralNetwork\\model.h5')
print("Model loaded from json")


#evaluate the new model ------------------------------------------------->
NNModel.compile(loss='binary_crossentropy', optimizer='rmsprop',metrics=['accuracy'])
score = NNModel.evaluate(testData_x,testData_y,verbose=0)
print("%s: %.2f%%" % (NNModel.metrics_names[1], score[1]*100))
##----------------------------------------------------------------------->

#evaluate with my own photo
NNModel.compile(loss='binary_crossentropy', optimizer='rmsprop',metrics=['accuracy'])
#image = imread('testImage.jpg')
#image = imread('testImage1.jpg')
image = imread('NeuralNetwork\\testImage2.jpg')
#image = plt.imread('testImage.jpg')
image= image[:,:,1]
image = image.reshape(1,1,28,28)
image.flatten()


print(NNModel.predict(image))


# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[2]:


import sys
print(sys.path)


# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:




