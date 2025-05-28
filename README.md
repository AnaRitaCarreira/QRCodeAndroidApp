
---

## 📱💻 QRCode App - Android + Flask Integration

### Android app to generate and scan QR Codes, sending the scanned data automatically to a local Flask server.

---

### 🧩 Features

#### 📱 Android App (Kotlin + Jetpack Compose)

* Generate QR Codes from text
* Scan QR Codes using the device camera or from gallery images
* Automatically sends scanned content to a Flask server

#### 🌐 Flask Server (Python)

* Receives QR Code data via HTTP `POST`
* Displays received QR Codes in a web interface
* Auto-refreshes the web page every few seconds

---

## 📦 Project Structure

```
/
├── android/              # Android project (Kotlin)
│   └── app/
├── server/               # Flask server (Python)
│   ├── app.py
│   ├── cert.pem / key.pem (for local HTTPS)
│   └── templates/
│       └── index.html
└── README.md
```

---

## 🚀 Getting Started

### 📱 1. Android App

#### Requirements:

* Android Studio
* Physical device or emulator (Android 7.0+)

#### Steps:

1. Open the project in Android Studio
2. Grant camera and storage permissions
3. Run the app

![screenshot](.appqrcode_demo.mp4) 


> When a QR Code is scanned, the app sends a `POST` request with the content to:
>
> `https://<YOUR_COMPUTER_IP>:8000/qr`

📶 **Your Android device and computer don't have to be connected to the same Wi-Fi network, but you need to change the public IP Address in the APP and in the Flask code.**

---

### 💻 2. Flask Server

#### Requirements:

* Python 3.7+
* Virtual environment (recommended)

#### Setup:

```bash
# Create and activate a virtual environment
python -m venv venv
source venv/bin/activate   # On Linux/macOS
venv\Scripts\activate.bat  # On Windows

# Install dependencies
pip install flask
```

#### Run the Flask server:

```bash
python app.py
```

> 🔐 **Using HTTPS:** The app uses HTTPS. Generate a local certificate using:

```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
```

---

## 🔗 Communication Flow

### Android sends:

```json
{
  "content": "scanned QR code data"
}
```

### Flask displays:

* The content is appended to an in-memory list and displayed on the main web page (`/`)
* The page refreshes every 3 seconds to show new data

---

## 🖥️ Web Interface (Flask)

![screenshot](.printscreen_web.PNG) 
---

## 🛠 Future Improvements

* [ ] Persistent storage (e.g., SQLite or PostgreSQL)
* [ ] QR Code upload via web interface
* [ ] Authentication or token-based security
* [ ] Real-time updates using WebSockets

---

## 👨‍💻 Author

Made with ❤️ by \AnaRitaCarreira

---

## 📄 License

MIT © \AnaRitaCarreira

---
