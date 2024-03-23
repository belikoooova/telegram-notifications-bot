package edu.java.scrapper.service.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JpaLinkServiceTest {
    @Mock
    private JpaLinkRepository linkRepository;

    @Mock
    private JpaChatRepository chatRepository;

    @InjectMocks
    private JpaLinkService jpaLinkService;

    private final long chatId = 1L;
    private final URI url = URI.create("http://example.com");
    private Link link;
    private Chat chat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        link = Link.builder()
            .url(url)
            .chats(new ArrayList<>())
            .lastCheckedAt(OffsetDateTime.now())
            .build();
        chat = Chat.builder().id(chatId).links(new ArrayList<>()).build();
    }

    @Test
    void addShouldConnectLinkToChat() {
        when(linkRepository.findByUrl(url)).thenReturn(Optional.empty());
        when(linkRepository.save(any(Link.class))).thenReturn(link);
        when(chatRepository.getReferenceById(chatId)).thenReturn(chat);

        jpaLinkService.add(chatId, url);

        verify(chatRepository).save(chat);
        verify(linkRepository).save(link);
        assertTrue(chat.getLinks().contains(link));
        assertTrue(link.getChats().contains(chat));
    }

    @Test
    void addShouldThrowExceptionWhenLinkAlreadyTracked() {
        chat.getLinks().add(link);
        link.getChats().add(chat);
        when(linkRepository.findByUrl(url)).thenReturn(Optional.of(link));
        when(chatRepository.getReferenceById(chatId)).thenReturn(chat);

        assertThrows(LinkAlreadyTrackedException.class, () -> jpaLinkService.add(chatId, url));
    }

    @Test
    void removeShouldDisconnectLinkFromChat() {
        chat.getLinks().add(link);
        link.getChats().add(chat);
        when(linkRepository.findByUrl(url)).thenReturn(Optional.of(link));
        when(chatRepository.getReferenceById(chatId)).thenReturn(chat);

        jpaLinkService.remove(chatId, url);

        verify(chatRepository).save(chat);
        assertTrue(link.getChats().isEmpty());
        assertTrue(chat.getLinks().isEmpty());
    }

    @Test
    void removeShouldThrowNoSuchLinkExceptionIfLinkNotFound() {
        when(linkRepository.findByUrl(url)).thenReturn(Optional.empty());

        assertThrows(NoSuchLinkException.class, () -> jpaLinkService.remove(chatId, url));
    }

    @Test
    void removeShouldThrowLinkNotTrackedExceptionIfLinkNotAssociatedWithChat() {
        when(linkRepository.findByUrl(url)).thenReturn(Optional.of(link));
        when(chatRepository.getReferenceById(chatId)).thenReturn(chat);

        assertThrows(LinkNotTrackedException.class, () -> jpaLinkService.remove(chatId, url));
    }

    @Test
    void listAllShouldReturnAllLinksForChat() {
        List<Link> expectedLinks = Arrays.asList(new Link(), new Link());
        when(linkRepository.findAll()).thenReturn(expectedLinks);

        var result = jpaLinkService.listAll(chatId);

        verify(linkRepository).findAll();
        assertEquals(expectedLinks.size(), result.size());
        assertTrue(result.containsAll(expectedLinks));
    }

    @Test
    void listAllShouldReturnEmptyIfNoLinks() {
        when(linkRepository.findAll()).thenReturn(Collections.emptyList());

        var result = jpaLinkService.listAll(chatId);

        verify(linkRepository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void listAllOldCheckedShouldReturnLinks() {
        Duration interval = Duration.ofHours(1);
        List<Link> expectedLinks = Arrays.asList(new Link(), new Link());
        when(linkRepository.findAllWithShiftInterval(any(OffsetDateTime.class))).thenReturn(expectedLinks);

        var result = jpaLinkService.listAllOldChecked(interval);

        verify(linkRepository).findAllWithShiftInterval(any(OffsetDateTime.class));
        assertEquals(expectedLinks.size(), result.size());
        assertTrue(result.containsAll(expectedLinks));
    }

    @Test
    void listAllOldCheckedShouldReturnEmptyIfNoOldLinks() {
        Duration interval = Duration.ofHours(1);
        when(linkRepository.findAllWithShiftInterval(any(OffsetDateTime.class))).thenReturn(Collections.emptyList());

        var result = jpaLinkService.listAllOldChecked(interval);

        verify(linkRepository).findAllWithShiftInterval(any(OffsetDateTime.class));
        assertTrue(result.isEmpty());
    }

    @Test
    void updateLastCheckedTimeShouldCallRepoMethod() {
        OffsetDateTime newDateTime = OffsetDateTime.now().minusHours(1);
        doNothing().when(linkRepository).updateLastCheckedTime(link.getId(), newDateTime);

        jpaLinkService.updateLastCheckedTime(link, newDateTime);

        verify(linkRepository).updateLastCheckedTime(link.getId(), newDateTime);
    }
}
