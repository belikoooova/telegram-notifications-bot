package edu.java.scrapper.service.link;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.AddLinkRequest;
import edu.java.scrapper.entity.dto.LinkResponse;
import edu.java.scrapper.entity.dto.ListLinkResponse;
import edu.java.scrapper.entity.dto.RemoveLinkRequest;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.repository.chat.ChatRepository;
import edu.java.scrapper.repository.link.LinkRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    public ListLinkResponse getAllLinks(Long chatId) {
        Chat chat = getChat(chatId);
        List<LinkResponse> linkResponses = chat.getLinks()
            .stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();
        return new ListLinkResponse(linkResponses, linkResponses.size());
    }

    public void addLink(Long chatId, AddLinkRequest request) {
        Chat chat = getChat(chatId);
        var link = linkRepository.findByUrl(request.getUrl())
            .orElseGet(() -> linkRepository.addLink(new Link(request.getUrl())));
        List<Link> links = chat.getLinks();
        if (links.contains(link)) {
            throw new LinkAlreadyTrackedException();
        }
        links.add(link);
    }

    public void deleteLink(Long chatId, RemoveLinkRequest request) {
        Chat chat = getChat(chatId);
        Link link = linkRepository.findByUrl(request.getUrl()).orElseThrow(NoSuchLinkException::new);
        linkRepository.deleteByUrl(request.getUrl());
        List<Link> links = chat.getLinks();
        if (!links.contains(link)) {
            throw new NoSuchLinkException();
        }
        links.remove(link);
    }

    private Chat getChat(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.getChatByID(chatId);
        if (optionalChat.isEmpty()) {
            throw new NoSuchChatException(chatId);
        }
        return optionalChat.get();
    }
}
