from ultralytics import YOLO
import torch

model = YOLO("yolo11n.pt")
model.export(format="onnx", opset=11)