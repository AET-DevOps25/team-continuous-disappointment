from flask import Blueprint, jsonify

generate_bp = Blueprint('generate', __name__)


@generate_bp.route('/api/generate', methods=['POST'])
def generate():
    return jsonify({'output': 'Hello World!'})
