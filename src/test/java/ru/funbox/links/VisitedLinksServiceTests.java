package ru.funbox.links;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.funbox.links.service.VisitedLinksService;
import ru.funbox.links.service.dto.VisitedDomainsDTO;
import ru.funbox.links.service.dto.VisitedLinksDTO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class VisitedLinksServiceTests {

    @Autowired
    VisitedLinksService visitedLinksService;

    @Test
    public void visitedLinksTest() {

        String[] links = new String[]{
                "https://ya.ru",
                "https://ya.ru?q=123",
                "funbox.ru",
                "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor"
        };

        long from = new Date().getTime();

        VisitedLinksDTO visitedLinksDTO = new VisitedLinksDTO(links);
        visitedLinksService.save(visitedLinksDTO);

        links = new String[]{
                "https://habr.com",
                "lenta.ru",
                "https://stackoverflow.com/"
        };

        visitedLinksDTO = new VisitedLinksDTO(links);
        visitedLinksService.save(visitedLinksDTO);

        final VisitedDomainsDTO visitedDomainsDTO = visitedLinksService.findByDateBetween(from, new Date().getTime());
        final String[] domains = visitedDomainsDTO.getDomains();

        // Проверяем что домены сохранились
        Assert.assertNotEquals(0, domains.length);

        // Проверям что не сохнаняются дубли
        Assert.assertFalse(duplicates(domains));

        // Проверяем что домены не содержат недопустимых символов
        final Pattern pattern = Pattern.compile("^(((?!-))(xn--|_{1,1})?[a-z0-9-]{0,61}[a-z0-9]{1,1}\\.)*(xn--)?([a-z0-9\\-]{1,61}|[a-z0-9-]{1,30}\\.[a-z]{2,})$");
        for (String domain: domains) {
            Matcher matcher = pattern.matcher(domain);
            Assert.assertTrue(matcher.find());
        }
    }

    boolean duplicates(final String[] domains)
    {
        Set<String> lump = new HashSet<>();
        for (String domain : domains)
        {
            if (lump.contains(domain)) return true;
            lump.add(domain);
        }
        return false;
    }
}
