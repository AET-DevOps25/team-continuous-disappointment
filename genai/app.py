import os
from dotenv import load_dotenv
from waitress import serve
from flask import Flask
from flask_cors import CORS
from controller.generate_controller import generate_bp

from config import Config

app = Flask(__name__)
CORS(app)
app.register_blueprint(generate_bp)

load_dotenv()

if __name__ == '__main__':
    use_waitress = Config.waitress
    # production environment
    if use_waitress:
        serve(app, host='0.0.0.0', port=8000)
    # local environment
    else:
        app.run(host='0.0.0.0', port=8000, debug=True)
