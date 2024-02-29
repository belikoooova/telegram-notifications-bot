package edu.java.scrapper.service.link;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.AddLinkRequest;
import edu.java.scrapper.entity.dto.LinkResponse;
import edu.java.scrapper.entity.dto.ListLinkResponse;
import edu.java.scrapper.entity.dto.RemoveLinkRequest;
import edu.java.scrapper.exception.LinkAlreadyTracksException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.repository.chat.ChatRepository;
import edu.java.scrapper.repository.link.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
            .collect(Collectors.toList());
        return new ListLinkResponse(linkResponses, linkResponses.size());
    }

    public void addLink(Long chatId, AddLinkRequest request) {
        Chat chat = getChat(chatId);
        Link link;
        if (linkRepository.findByUrl(request.getUrl()).isEmpty()) {
            link = new Link(request.getUrl());
            linkRepository.addLink(link);
        } else {
            link = linkRepository.findByUrl(request.getUrl()).get();
        }
        List<Link> links = chat.getLinks();
        if (links.contains(link)) {
            throw new LinkAlreadyTracksException();
        }
        links.add(link);
    }

    public void deleteLink(Long chatId, RemoveLinkRequest request) {
        Chat chat = getChat(chatId);
        Link link = linkRepository.findByUrl(request.getUrl()).orElseThrow(NoSuchLinkException::new);
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
