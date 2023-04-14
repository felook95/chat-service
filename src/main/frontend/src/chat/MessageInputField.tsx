import { TextField } from '@mui/material';
import React, { KeyboardEvent, useState } from 'react';
import Message from './Message';

interface Props {
  onSendMessage: (message: Message) => void;
}

const MessageInputField = ({ onSendMessage }: Props) => {
  const [message, setMessage] = useState('');

  const handleKeyDown = (keyDownEvent: KeyboardEvent<HTMLImageElement>) => {
    if (keyDownEvent.key === 'Enter' && !keyDownEvent.shiftKey) {
      keyDownEvent.preventDefault();

      const messageToSend: Message = {
        senderId: '1',
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
