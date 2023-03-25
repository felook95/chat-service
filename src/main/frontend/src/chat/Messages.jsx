import React, { useEffect, useState } from 'react';
import { Avatar, List, ListItem, Typography } from '@mui/material';

const Messages = ({ conversationId }) => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    fetch(`/conversation/${conversationId}`)
      .then((data) => data.json())
      .then(console.log);
    const mockMessages = [];
    setMessages(mockMessages);
  }, [conversationId]);

  return (
    <List>
      {messages.map((message) => {
        return (
          <ListItem key={crypto.randomUUID()}>
            <Avatar>{message.sender}</Avatar>
            <Typography variant="h6">{message.content}</Typography>
          </ListItem>
        );
      })}
    </List>
  );
};

export default Messages;
