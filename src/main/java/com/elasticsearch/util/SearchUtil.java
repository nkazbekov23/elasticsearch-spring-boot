package com.elasticsearch.util;

import com.elasticsearch.dto.SearchRequestDto;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

public class SearchUtil {


    private SearchUtil() {}

    public static SearchRequest searchRequest(final String indexName, final SearchRequestDto dto) {
        try {
            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(queryBuilder(dto));

            if (dto.getSortedBy() != null) {
                builder.sort(
                        dto.getSortedBy(),
                        dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC);
            }

            final SearchRequest request = new SearchRequest(indexName);

            request.source(builder);
            return request;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final String field,
                                                   final Date date) {
        try {
            final SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(field, date));

            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
}



    private static QueryBuilder queryBuilder(final SearchRequestDto dto) {
        if (dto == null) {
            return null;
        }

        final List<String> fields = dto.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        if (fields.size() > 0) {
            final MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.AND);
            fields.forEach(queryBuilder::field);
            return queryBuilder;
        }

        return fields.stream()
                .findFirst()
                .map(field ->
                        QueryBuilders.matchQuery(field, dto.getSearchTerm())
                                .operator(Operator.AND))
                .orElse(null);
    }


    private static QueryBuilder getQueryBuilder(final String field, final Date date) {
        return QueryBuilders.rangeQuery(field).gte(date);
    }
}
