import os
import requests
from flask import Flask, request, jsonify

app = Flask(__name__)

API_KEY = os.environ.get("GOOGLE_API_KEY")

if not API_KEY:
    raise ValueError("No GOOGLE_API_KEY set. Please set the environment variable.")


@app.route('/get_nearby_places', methods=['POST'])
def get_nearby_places():
    """
    This endpoint receives latitude/longitude, queries the Google Places API,
    and returns a list of nearby places including a photo URL.
    """
    if not request.is_json:
        return jsonify({"error": "Missing JSON in request"}), 400

    data = request.get_json()
    latitude = data.get('latitude')
    longitude = data.get('longitude')
    place_type = data.get('place_type', 'tourist_attraction')

    if not latitude or not longitude:
        return jsonify({"error": "Missing latitude or longitude"}), 400

    try:
        url = "https://places.googleapis.com/v1/places:searchNearby"
        
        payload = {
            "includedTypes": [place_type],
            "maxResultCount": 10,
            "locationRestriction": {
                "circle": {
                    "center": {
                        "latitude": latitude,
                        "longitude": longitude
                    },
                    "radius": 5000.0
                }
            }
        }
        
        headers = {
            'Content-Type': 'application/json',
            'X-Goog-Api-Key': API_KEY,
            'X-Goog-FieldMask': 'places.displayName,places.formattedAddress,places.rating,places.userRatingCount,places.photos'
        }

        response = requests.post(url, json=payload, headers=headers)
        response.raise_for_status() 
        api_response = response.json()

        places = []
        if 'places' in api_response:
            for place in api_response['places']:
                photo_url = None
                if 'photos' in place and len(place['photos']) > 0:
                    photo_name = place['photos'][0]['name']
                    photo_url = f"https://places.googleapis.com/v1/{photo_name}/media?maxHeightPx=200&key={API_KEY}"

                places.append({
                    'name': place.get('displayName', {}).get('text'),
                    'address': place.get('formattedAddress'),
                    'rating': place.get('rating'),
                    'user_rating_count': place.get('userRatingCount'),
                    'photo_url': photo_url 
                })

        return jsonify(places)

    except requests.exceptions.RequestException as e:
        return jsonify({"error": f"API request failed: {e}"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':

    app.run(host='0.0.0.0', port=5000)
