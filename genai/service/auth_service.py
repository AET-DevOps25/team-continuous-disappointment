import requests
from fastapi import HTTPException, Header
from typing import Optional
from logger import logger


class UserInfo:
    def __init__(self, user_id: int, username: str):
        self.user_id = user_id
        self.username = username


async def get_current_user(authorization: Optional[str] = Header(None)) -> UserInfo:
    """
    Extract user information from the Authorization header.
    
    This function validates the OAuth token by calling the user service
    and returns the user information including user_id.
    """
    if not authorization:
        logger.error("Authorization header is missing")
        raise HTTPException(status_code=401, detail="Authorization header required")
    
    if not authorization.startswith("Bearer "):
        logger.error("Invalid authorization header format")
        raise HTTPException(status_code=401, detail="Invalid authorization header format")
    
    token = authorization.split(" ")[1]
    
    user_service_urls = [
    "http://localhost:8081/user/info",   # dev
    "http://user-service:8081/user/info" # prod      
    ]

    for url in user_service_urls:
        try:
            response = requests.get(
                url,
                headers={"Authorization": f"Bearer {token}"},
                timeout=10
            )
            if response.status_code == 200:
                user_data = response.json()
                logger.info(f"User authenticated: {user_data.get('username')}")
                return UserInfo(
                    user_id=user_data.get("id"),
                    username=user_data.get("username")
                )
            else:
                logger.error(f"{url} returned status {response.status_code}")
        except requests.exceptions.RequestException as e:
            logger.warning(f"Failed to reach {url}: {e}")

    raise HTTPException(status_code=500, detail="Authentication service unavailable")
