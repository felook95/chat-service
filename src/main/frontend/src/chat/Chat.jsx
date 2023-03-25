import { Container } from '@mui/material';
import React from 'react';
import { useParams } from 'react-router-dom';
import Messages from './Messages';

const Chat = () => {
  const { userName } = useParams();

  return (
    <Container>
      <Messages conversationId="30984843884124536172001826548" />
    </Container>
  );
};

export default Chat;
