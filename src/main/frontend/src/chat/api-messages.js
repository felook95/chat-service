const getMessages = (conversationId) => {
  return fetch(`/conversation/${conversationId}/messages`).then((data) =>
    data.json()
  );
};

export { getMessages };
