from flask import Flask, request, jsonify, render_template
from flask import send_from_directory
import os

app = Flask(__name__)

qr_codes = []

@app.route('/qr', methods=['POST'])
def qr():
    data = request.get_json()
    if not data or "content" not in data:
        return jsonify({"error": "Invalid request"}), 400

    content = data["content"]
    print("QR recebido:", content)
    qr_codes.append(content)
    return jsonify({"status": "ok"}), 200

@app.route('/qr-codes')
def get_qr_codes():
    return jsonify(qr_codes)

@app.route('/')
def home():
    return render_template("index.html")

if __name__ == '__main__':
    app.run(ssl_context=('cert.pem', 'key.pem'), host='0.0.0.0', port=8000)


#source ./venv/bin/activate
#gunicorn --bind 0.0.0.0:8000 app:app
