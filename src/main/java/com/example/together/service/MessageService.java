package com.example.together.service;

import com.example.together.dto.response.GroupMessageResponse;
import com.example.together.dto.response.PrivateMessageResponse;
import com.example.together.exception.AppException;
import com.example.together.exception.ErrorCode;
import com.example.together.mapper.GroupMessageMapper;
import com.example.together.mapper.PrivateMessageMapper;
import com.example.together.model.User;
import com.example.together.repository.GroupMessageRepository;
import com.example.together.repository.PrivateMessageRepository;
import com.example.together.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {
    UserRepository userRepository;
    GroupMessageRepository groupMessageRepository;
    GroupMessageMapper groupMessageMapper;
    PrivateMessageRepository privateMessageRepository;
    PrivateMessageMapper privateMessageMapper;

    public List<Object> getSortedMessagesForUser(String receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER));

        // Lấy danh sách tin nhắn nhóm và tin nhắn riêng tư
        List<GroupMessageResponse> gmr = groupMessageRepository.findMessagesByGroupsForUser(receiverId)
                .stream()
                .map(groupMessageMapper::toGroupMessageResponse)
                .collect(Collectors.toList());

        List<PrivateMessageResponse> pmr = privateMessageRepository.findMessagesByReceiverIdOrderBySentAt(receiverId)
                .stream()
                .map(privateMessageMapper::toPrivateMessageResponse)
                .collect(Collectors.toList());

        // Kết hợp tất cả các tin nhắn vào một danh sách
        List<Object> allMessages = new ArrayList<>();
        allMessages.addAll(gmr);
        allMessages.addAll(pmr);

        // Sắp xếp theo `sentAt`, xử lý null và đảo ngược thứ tự
        allMessages.sort(Comparator.comparing(message -> {
            if (message instanceof GroupMessageResponse) {
                return ((GroupMessageResponse) message).getSentAt();
            } else if (message instanceof PrivateMessageResponse) {
                return ((PrivateMessageResponse) message).getSentAt();
            }
            return null;
        }, Comparator.reverseOrder()));

        return allMessages;
    }

}
