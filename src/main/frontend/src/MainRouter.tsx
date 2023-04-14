import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Chat from './chat/Chat';
import Login from './auth/Login';

const MainRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/chat/:conversationId" element={<Chat />} />
    </Routes>
  );
};

export default MainRouter;
