from flask import Flask, request, jsonify, render_template_string

app = Flask(__name__)

# Lista para armazenar os QR Codes recebidos
qr_codes = []

@app.route('/qr', methods=['POST'])
def qr():
    if not request.is_json:
        return jsonify({"error": "Request body is not JSON"}), 400

    data = request.get_json()
    if not data:
        return jsonify({"error": "Empty JSON body"}), 400

    if "content" not in data:
        return jsonify({"error": "Missing 'content' field in JSON"}), 400

    content = data['content']
    print("QR recebido:", content)

    # Armazena o conteúdo na lista
    qr_codes.append(content)

    return jsonify({"status": "ok"}), 200

# Página web para mostrar os QR Codes recebidos
@app.route('/')
def home():
    html = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>QR Codes Recebidos</title>
        <style>
            body { font-family: Arial, sans-serif; margin: 40px; }
            h1 { color: #333; }
            ul { list-style-type: none; padding: 0; }
            li { padding: 8px 0; border-bottom: 1px solid #ccc; }
        </style>
    </head>
    <body>
        <h1>QR Codes Recebidos</h1>
        {% if qr_codes %}
            <ul>
            {% for code in qr_codes %}
                <li>{{ code }}</li>
            {% endfor %}
            </ul>
        {% else %}
            <p>Nenhum QR Code recebido ainda.</p>
        {% endif %}
    </body>
    </html>
    """
    return render_template_string(html, qr_codes=qr_codes)

if __name__ == '__main__':
    app.run(ssl_context=('cert.pem', 'key.pem'),host='0.0.0.0', port=8000)

#source ./venv/bin/activate
#gunicorn --bind 0.0.0.0:8000 app:app
