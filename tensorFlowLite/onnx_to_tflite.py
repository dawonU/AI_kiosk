import onnx
from onnx_tf.backend import prepare

onnx_model = onnx.load("yolo11n.onnx")
tf_rep = prepare(onnx_model)
tf_rep.export_graph("yolo_tf_model")