import os
from dotenv import load_dotenv
from waitress import serve
from flask import Flask
from controller.generate_controller import generate_bp

app = Flask(__name__)
app.register_blueprint(generate_bp)

load_dotenv()

if __name__ == '__main__':
    use_waitress = os.getenv("USE_WAITRESS", "false").lower() == "true"
    # production environment
    if use_waitress:
        serve(app, host='0.0.0.0', port=8000)
    # local environment
    else:
        app.run(host='0.0.0.0', port=8000, debug=True)
