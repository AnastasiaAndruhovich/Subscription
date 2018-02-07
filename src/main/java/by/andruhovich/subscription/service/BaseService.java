package by.andruhovich.subscription.service;

public abstract class BaseService {
    static final int ENTITY_COUNT_FOR_ONE_PAGE = 10;

    int findStartIndex(String pageNumber, int entityCount) {
        int page = Integer.parseInt(pageNumber);
        int startIndex = entityCount - ENTITY_COUNT_FOR_ONE_PAGE * page;
        return  startIndex < 0 ? 0 : startIndex;
    }
}
