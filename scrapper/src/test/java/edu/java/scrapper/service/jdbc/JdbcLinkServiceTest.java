package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JdbcLinkServiceTest {
    @Mock
    private JdbcLinkRepository linkRepository;

    @InjectMocks
    private JdbcLinkService jdbcLinkService;

    private final long ID = 1L;
    private final URI URL = URI.create("http://example.com");
    private Link link;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        link = new Link();
        link.setId(ID);
        link.setUrl(URL);
        link.setLastCheckedAt(OffsetDateTime.now());
        link.setLastUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void addShouldConnectLinkToChat() {
        when(linkRepository.findLinkByUrl(URL)).thenReturn(Optional.of(link));
        when(linkRepository.add(any(Link.class))).thenReturn(link);

        Link result = jdbcLinkService.add(ID, URL);

        verify(linkRepository).connectLinkToChat(ID, link.getId());
        assertEquals(link, result);
    }

    @Test
    void addShouldThrowExceptionOnDuplicateKey() {
        when(linkRepository.findLinkByUrl(URL)).thenReturn(Optional.of(link));
        doThrow(DuplicateKeyException.class).when(linkRepository).connectLinkToChat(anyLong(), anyLong());

        assertThrows(LinkAlreadyTrackedException.class, () -> jdbcLinkService.add(ID, URL));
    }

    @Test
    void removeShouldDisconnectLinkFromChat() {
        when(linkRepository.findLinkByUrl(URL)).thenReturn(Optional.of(link));

        Link result = jdbcLinkService.remove(ID, URL);

        verify(linkRepository).disconnectLinkToChat(ID, link.getId());
        assertEquals(link, result);
    }

    @Test
    void removeShouldThrowNoSuchLinkExceptionIfLinkNotFound() {
        when(linkRepository.findLinkByUrl(URL)).thenReturn(Optional.empty());

        assertThrows(NoSuchLinkException.class, () -> jdbcLinkService.remove(ID, URL));
    }

    @Test
    void removeShouldThrowLinkNotTrackedExceptionIfLinkNotAssociatedWithChat() {
        when(linkRepository.findLinkByUrl(URL)).thenReturn(Optional.of(link));
        doThrow(EmptyResultDataAccessException.class).when(linkRepository).disconnectLinkToChat(anyLong(), anyLong());

        assertThrows(LinkNotTrackedException.class, () -> jdbcLinkService.remove(ID, URL));
    }

    @Test
    void listAllShouldReturnAllLinksForChat() {
        List<Link> expectedLinks = Arrays.asList(new Link(), new Link());
        when(linkRepository.findAllLinksByChatId(ID)).thenReturn(expectedLinks);

        Collection<Link> result = jdbcLinkService.listAll(ID);

        verify(linkRepository).findAllLinksByChatId(ID);
        assertEquals(expectedLinks.size(), result.size());
        assertTrue(result.containsAll(expectedLinks));
    }

    @Test
    @DisplayName("listAll should return empty list if no links found")
    void listAllShouldReturnEmptyIfNoLinks() {
        when(linkRepository.findAllLinksByChatId(ID)).thenReturn(Collections.emptyList());

        Collection<Link> result = jdbcLinkService.listAll(ID);

        verify(linkRepository).findAllLinksByChatId(ID);
        assertTrue(result.isEmpty());
    }

    @Test
    void listAllOldCheckedShouldReturnLinks() {
        Duration interval = Duration.ofHours(1);
        List<Link> expectedLinks =
            Arrays.asList(new Link(), new Link()); // Assume Link objects are properly initialized
        when(linkRepository.findAllWithShitInterval(interval)).thenReturn(expectedLinks);

        Collection<Link> result = jdbcLinkService.listAllOldChecked(interval);

        verify(linkRepository).findAllWithShitInterval(interval);
        assertEquals(expectedLinks.size(), result.size());
        assertTrue(result.containsAll(expectedLinks));
    }

    @Test
    void listAllOldCheckedShouldReturnEmptyIfNoOldLinks() {
        Duration interval = Duration.ofHours(1);
        when(linkRepository.findAllWithShitInterval(interval)).thenReturn(Collections.emptyList());

        Collection<Link> result = jdbcLinkService.listAllOldChecked(interval);

        verify(linkRepository).findAllWithShitInterval(interval);
        assertTrue(result.isEmpty());
    }
}
