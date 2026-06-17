# Abstract away LLMClient interfaces

from abc import ABC, abstractmethod
from pydantic import BaseModel
from typing import TypeVar, Type

T = TypeVar("T", bound=BaseModel)

class LLMClient(ABC):
    """
    Language agnostic base class
    """
    
    @abstractmethod
    async def generate_structured_json(
        self,
        prompt: str,
        response_schema: Type[T],
        model_name: str | None = None,
    ) -> T:
        """
        Generate structured JSON output from the provider client by using the prompt and specified model 
        """
        pass