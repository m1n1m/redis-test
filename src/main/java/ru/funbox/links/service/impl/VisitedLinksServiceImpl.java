package ru.funbox.links.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import ru.funbox.links.service.VisitedLinksService;
import ru.funbox.links.service.dto.VisitedDomainsDTO;
import ru.funbox.links.service.dto.VisitedLinksDTO;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VisitedLinksServiceImpl implements VisitedLinksService {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final ZSetOperations<String, String> stringStringZSetOperations;
    private final Pattern domainPattern;
    private final String KEY = "VISITED_DOMAINS";

    public VisitedLinksServiceImpl(RedisTemplate<String, String> stringStringRedisTemplate) {
        this.stringRedisTemplate = stringStringRedisTemplate;
        stringStringZSetOperations = stringRedisTemplate.opsForZSet();
        domainPattern = Pattern.compile("^(?:https?:\\/\\/)?(?:[^@\\/\\n]+@)?(?:www\\.)?([^:\\/?\\n]+)");
    }

    @Override
    public @NotNull VisitedLinksDTO save(@NotNull VisitedLinksDTO visitedLinksDTO) {

        final long time = new Date().getTime();
        final Set<String> domains = new HashSet<>();

        for(String url: visitedLinksDTO.getLinks()) {

            Matcher matcher = domainPattern.matcher(url);
            if (matcher.find()) {

                final String domain = matcher.group(0)
                        .replaceAll("http://", "")
                        .replaceAll("https://", "");

                domains.add(domain);
            }
        }

        final ObjectMapper om = new ObjectMapper();

        try {
            final VisitedDomainsDTO visitedDomainsDTO = new VisitedDomainsDTO(domains, time);
            stringStringZSetOperations.add(KEY, om.writeValueAsString(visitedDomainsDTO), time);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error convert domain array to json");
        }

        return visitedLinksDTO;
    }

    @Override
    public @NotNull VisitedDomainsDTO findByDateBetween(@NotNull Long from, @NotNull Long to) {

        final Set<String> domains = new HashSet<>();
        final Set<String> result = stringStringZSetOperations.rangeByScore(KEY, from, to);

        final ObjectMapper om = new ObjectMapper();

        for (String jsonDomains: result) {

            try {
                VisitedDomainsDTO visited = om.readValue(jsonDomains, VisitedDomainsDTO.class);
                domains.addAll(Arrays.asList(visited.getDomains()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new VisitedDomainsDTO(domains);
    }

}
