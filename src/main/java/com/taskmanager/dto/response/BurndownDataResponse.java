package com.taskmanager.dto.response;
import java.util.List;
public record BurndownDataResponse(
    Long sprintId, String sprintName,
    List<BurndownPoint> byTask,
    List<BurndownPoint> byPoint
) {}
