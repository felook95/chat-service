import { TextField } from '@mui/material';
import React, { useState } from 'react';

const MessageInputField = ({ onSendMessage }) => {
  const [message, setMessage] = useState('');

  const handleKeyDown = (keyDownEvent) => {
    if (keyDownEvent.key === 'Enter' && !keyDownEvent.shiftKey) {
      keyDownEvent.preventDefault();

      const messageToSend = {
        senderId: 1,
        content: message,
      };
      onSendMessage(messageToSend);
      setMessage('');
    }
  };

  return (
    <TextField
      autoFocus
      multiline
      placeholder="Type a message here"
      size="small"
      fullWidth
      value={message}
      onChange={(event) => setMessage(event.target.value)}
      required
      onKeyDown={handleKeyDown}
    />
  );
};

export default MessageInputField;
