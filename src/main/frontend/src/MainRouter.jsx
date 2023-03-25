import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Chat from './chat/Chat';
import Login from './auth/Login';

const MainRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/chat/:userName" element={<Chat />} />
    </Routes>
  );
};

export default MainRouter;
