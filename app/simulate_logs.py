from faker import Faker
import pandas as pd
import random

fake = Faker()

LABELS = ["normal", "port_scan", "ddos", "brute_force"]

def generate_logs(n=800):
    logs = []

    for _ in range(n):
        logs.append([
            fake.ipv4(),
            random.choice([22, 80, 443, 8080, 53]),
            random.randint(50, 2000),
            random.choice(["TCP", "UDP"]),
            random.choices(LABELS, weights=[0.7,0.1,0.1,0.1])[0]
        ])

    df = pd.DataFrame(logs, columns=[
        "src_ip", "dst_port", "packet_size", "protocol", "label"
    ])

    df.to_csv("data/network_logs.csv", index=False)
    print("✔ Logs created → data/network_logs.csv")

if __name__ == "__main__":
    
    generate_logs()
