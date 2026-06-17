# Google Gemini implementation of LLMClient
from pydantic import BaseModel
from typing import TypeVar, Type
import google.genai as genai

from app.core.config import AppSettings
from app.core.exceptions import LLMProviderError,LLMServiceException
from app.infra.llm.base import LLMClient

# any subtype of BaseModel is acceptable as structured output
T = TypeVar("T", bound=BaseModel)

class GeminiLLMClient(LLMClient):

    def __init__(self):
        self.client = genai.Client(
            api_key=AppSettings.llm_api_key.get_secret_value()
        )
    
    async def generate_structured_json(
        self,
        prompt: str,
        response_schema: Type[T],
        model_name: str | None = None
    ) -> T:
        selected_model = model_name or AppSettings.llm_model 
        
        try:
            response = await self.client.aio.models.generate_content(
                model=selected_model,
                contents=prompt,
                config=genai.types.GenerateContentConfig(
                    response_mime_type="application/json",
                    response_schema=response_schema,
                    temperature=0.1
                )
            )
        
            if not response.text:
                raise LLMProviderError(
                    selected_model,
                    "" 
                )
        
            return response_schema.model_validate_json(response.text)
        except Exception as e:
            raise LLMServiceException(
                "eror getting AI inference",
                500
                )

