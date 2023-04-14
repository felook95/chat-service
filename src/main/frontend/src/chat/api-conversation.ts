import Message from './Message';

const startConversation = () => {
  return fetch('/conversation/', { method: 'POST' }).then((data) =>
    data.json()
  );
};

const joinToConversation = (conversationId: string, participantId: string) => {
  return fetch(
    `/conversation/${conversationId}/participants/${participantId}`,
    { method: 'POST' }
  );
};

const getMessagesPaged = (
  conversationId: string,
  pageIndex: number,
  pageSize: number
) => {
  return fetch(
    `/conversation/${conversationId}/messages?pageIndex=${pageIndex}&pageSize=${pageSize}`
  ).then((data) => data.json());
};

const sendMessage = (conversationId: string, message: Message) => {
  return fetch(`/conversation/${conversationId}/messages`, {
    method: 'POST',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(message),
  });
};

const getParticipants = (conversationId: string) => {
  return fetch(`/conversation/${conversationId}/participants`).then((data) =>
    data.json()
  );
};

export {
  startConversation,
  joinToConversation,
  getMessagesPaged,
  sendMessage,
  getParticipants,
};
