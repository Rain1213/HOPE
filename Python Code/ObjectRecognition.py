from imageai.Detection import ObjectDetection
import os
from firebase import Firebase
import requests
import time

config = {
    "apiKey": "AIzaSyCzphhFlPG78RzXO5lGpzyzMt2ahq0xngI",
    "authDomain": "lostandfound-1adc1.firebaseapp.com",
    "databaseURL": "https://lostandfound-1adc1.firebaseio.com",
    "projectId": "lostandfound-1adc1",
    "storageBucket": "lostandfound-1adc1.appspot.com",
    "messagingSenderId": "744472946418",
    "appId": "1:744472946418:web:8df69d71ec0511a5bdd26b"
}

img_names = []
firebase = Firebase(config)
storage = firebase.storage()
db = firebase.database()
project = "FoundItemData"
names = db.child(project).get()
for name in names.each():
    entries = project + "/" + str(name.key())
    values = db.child(entries).get()
    for value in values.each():
        if str(value.key()) == "imgUrl":
            img_names.append(value.val())

counter = int(0)
for image in img_names:
    img_data = requests.get(image).content
    with open("image"+str(counter)+".jpg", 'wb') as handler:
        handler.write(img_data)
        time.sleep(1)
    counter += 1

execution_path = os.getcwd()

detector = ObjectDetection()
detector.setModelTypeAsRetinaNet()
detector.setModelPath(os.path.join(execution_path, "resnet50_coco_best_v2.0.1.h5"))
detector.loadModel()

counter = 0
project = "FoundItemData"
names = db.child(project).get()
for name in names.each():
    detections = detector.detectObjectsFromImage(input_image=os.path.join(execution_path, "image"+str(counter)+".jpg"),
                                                 output_image_path=os.path.join(execution_path, "imagenew"+str(counter)+".jpg"))
    entries = project + "/" + str(name.key())
    db.child(entries).get()
    counter += 1
    for eachObject in detections:
        db.child(entries).update({"type": eachObject["name"]})
