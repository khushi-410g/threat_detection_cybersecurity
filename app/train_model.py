import pandas as pd
from sklearn.preprocessing import LabelEncoder
from sklearn.svm import SVC
from joblib import dump
import hashlib

def ip_hash(ip):
    return int(hashlib.md5(ip.encode()).hexdigest(), 16) % 10**8

df = pd.read_csv("data/network_logs.csv")

df["src_ip"] = df["src_ip"].apply(ip_hash)

enc = LabelEncoder()
df["protocol"] = enc.fit_transform(df["protocol"])

X = df[["src_ip", "dst_port", "packet_size", "protocol"]]
y = df["label"]

model = SVC(probability=True)
model.fit(X, y)

dump(model, "model/threat_model.pkl")
dump(enc, "model/protocol_encoder.pkl")

print("✔ Model saved → model/threat_model.pkl")
