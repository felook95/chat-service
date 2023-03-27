import React, { useEffect, useState } from 'react';
import { Avatar, List, ListItem, Typography } from '@mui/material';
import { getMessages } from './api-messages';

const Messages = ({ conversationId }) => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    getMessages(conversationId).then(setMessages);
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
