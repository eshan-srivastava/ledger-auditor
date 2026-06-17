"""
SCHEMAS ARE REQUEST RESPONSE DTOs USED BY API ENDPOINTS OF THIS PROGRAM's FASTAPI SERVER
"""
from pydantic import BaseModel

class InsightResponse(BaseModel):
    """
    Common InsightResponse can be used by any kind of insight subroute
    """
    insight_requested: str
    

    model_config = {"from_attributes": True}
    
    
class InsightRequest(BaseModel):
    """
    Common InsightRequest can be used by any kind of insight subroute to read the body
    """
    insight_requested: str
    

    model_config = {"from_attributes": True}