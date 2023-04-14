import React, { useEffect, useState } from 'react';
import { Avatar, Box, List, ListItem, Typography } from '@mui/material';
import { getMessagesPaged } from './api-conversation';
import Message from './Message';

interface Props {
  conversationId: string;
}

const Messages = ({ conversationId }: Props) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [currentPage] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      getMessagesPaged(conversationId, currentPage, 100).then(setMessages);
    }, 1000);
    return () => clearInterval(interval);
  }, [conversationId, currentPage]);

  return (
    <List>
      {messages.map((message) => {
        return (
          <ListItem key={crypto.randomUUID()}>
            <Box mr={1}>
              <Avatar>{message.senderId}</Avatar>
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
