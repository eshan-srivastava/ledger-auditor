from starlette.status import HTTP_500_INTERNAL_SERVER_ERROR
from fastapi import APIRouter, status, Depends, HTTPException
from app.domain.schemas import InsightRequest, InsightResponse

router = APIRouter(
    prefix="/insights",
    tags=["Insights Reviewer"]
)

@router.post(
    path="/spending-summary",
    response_model=InsightResponse,
    status_code=status.HTTP_200_OK    
)
async def generate_spending_summary(
    payload: InsightRequest,
    service: InsightService = Depends()
):
    try:
        # result = await service.build_financial_insights(payload)
        # return result
        pass
    except Exception as e:
        raise HTTPException(
            status_code=HTTP_500_INTERNAL_SERVER_ERROR,
            detail=str(e)
        )