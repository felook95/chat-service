import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import MessageInputField from './MessageInputField';
import Messages from './Messages';
import { joinToConversation, sendMessage } from './api-conversation';
import Grid from '@mui/material/Unstable_Grid2/Grid2';
import { Box } from '@mui/material';
import Message from './Message';

const Chat = () => {
  const { conversationId } = useParams();

  useEffect(() => {
    joinToConversation(conversationId!, '1');
  }, [conversationId]);

  const handleSendMessage = (messageToSend: Message) => {
    sendMessage(conversationId!, messageToSend);
  };

  return (
    <Grid container marginX={1} height={'100%'} direction={'column'}>
      <Grid sx={{ flex: 1, display: 'flex', overflowY: 'auto' }} xs={12}>
        <Messages conversationId={conversationId!} />
      </Grid>
      <Grid>
        <Box mb={1}>
          <MessageInputField onSendMessage={handleSendMessage} />
        </Box>
      </Grid>
    </Grid>
  );
};

export default Chat;
