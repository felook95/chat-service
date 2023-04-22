import Conversation from './Conversation';
import Message from './Message';
import axios from 'axios';

const apiClient = axios.create();

const startConversation = () => {
  return apiClient.post<Conversation>('/conversation').then((res) => res.data);
};

const joinToConversation = (conversationId: string, participantId: string) => {
  return apiClient
    .post<Conversation>(
      `/conversation/${conversationId}/participants/${participantId}`
    )
    .then((res) => res.data);
};

const getMessagesPaged = (
  conversationId: string,
  pageIndex: number,
  pageSize: number
) => {
  return apiClient
    .get<Message[]>(
      `/conversation/${conversationId}/messages?pageIndex=${pageIndex}&pageSize=${pageSize}`
    )
    .then((res) => res.data);
};

const sendMessage = (conversationId: string, message: Message) => {
  return apiClient
    .post<Message>(`/conversation/${conversationId}/messages`, message)
    .then((res) => res.data);
};

export { startConversation, joinToConversation, getMessagesPaged, sendMessage };
