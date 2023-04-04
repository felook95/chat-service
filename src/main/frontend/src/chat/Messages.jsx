import React, { useEffect, useState } from 'react';
import { Avatar, Box, List, ListItem, Typography } from '@mui/material';
import { getMessagesPaged } from './api-conversation';

const Messages = ({ conversationId }) => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const interval = setInterval(() => {
      getMessagesPaged(conversationId, currentPage, 100).then(setMessages);
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  return (
    <List>
      {messages.map((message) => {
        return (
          <ListItem key={crypto.randomUUID()}>
            <Box mr={1}>
              <Avatar>{message.sender}</Avatar>
            </Box>
            <Typography sx={{ whiteSpace: 'pre' }} variant="h6">
              {message.content}
            </Typography>
          </ListItem>
        );
      })}
    </List>
  );
};

export default Messages;
