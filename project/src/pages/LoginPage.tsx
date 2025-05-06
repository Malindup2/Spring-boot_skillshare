import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Lightbulb, ArrowLeft, AlertCircle } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';

const LoginPage: React.FC = () => {
  const { login, isLoading } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);
  
  const handleLogin = async (provider: string) => {
    setError(null);
    try {
      await login(provider);
      navigate('/');
    } catch (error) {
      console.error('Login failed:', error);
      setError(error instanceof Error ? error.message : 'Failed to login. Please try again.');
    }
  };
  
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-xl shadow-lg overflow-hidden max-w-md w-full">
        <div className="relative">
          <button
            onClick={() => navigate('/')}
            className="absolute top-4 left-4 p-2 rounded-full bg-white/70 text-gray-700 hover:bg-white/90 transition-colors"
          >
            <ArrowLeft className="h-5 w-5" />
          </button>
          <div className="bg-gradient-to-r from-blue-500 to-indigo-600 p-8 text-center">
            <div className="inline-flex items-center justify-center h-16 w-16 rounded-full bg-white/20 mb-4">
              <Lightbulb className="h-8 w-8 text-white" />
            </div>
            <h1 className="text-2xl font-bold text-white mb-2">Welcome to SkillShare</h1>
            <p className="text-blue-100">Share your skills, track your progress, connect with learners</p>
          </div>
        </div>
        
        <div className="p-6">
          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg flex items-center text-red-600">
              <AlertCircle className="h-5 w-5 mr-2" />
              <span className="text-sm">{error}</span>
            </div>
          )}
          <div className="text-center mb-6">
            <h2 className="text-xl font-semibold text-gray-800">Sign in to your account</h2>
            <p className="text-gray-500 text-sm mt-1">
              Use your social media account to continue
            </p>
          </div>
          
          <div className="space-y-3">
            <button
              onClick={() => handleLogin('google')}
              disabled={isLoading}
              className="flex items-center justify-center w-full py-3 px-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
            >
              <img 
                src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg" 
                alt="Google" 
                className="h-5 w-5 mr-3"
              />
              <span className="text-gray-700 font-medium">Continue with Google</span>
            </button>
            
            <button
              onClick={() => handleLogin('facebook')}
              disabled={isLoading}
              className="flex items-center justify-center w-full py-3 px-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
            >
              <img 
                src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/facebook.svg" 
                alt="Facebook" 
                className="h-5 w-5 mr-3"
              />
              <span className="text-gray-700 font-medium">Continue with Facebook</span>
            </button>
            
            <button
              onClick={() => handleLogin('twitter')}
              disabled={isLoading}
              className="flex items-center justify-center w-full py-3 px-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
            >
              <svg className="h-5 w-5 mr-3 text-[#1DA1F2]" viewBox="0 0 24 24" fill="currentColor">
                <path d="M22.46,6C21.69,6.35 20.86,6.58 20,6.69C20.88,6.16 21.56,5.32 21.88,4.31C21.05,4.81 20.13,5.16 19.16,5.36C18.37,4.5 17.26,4 16,4C13.65,4 11.73,5.92 11.73,8.29C11.73,8.63 11.77,8.96 11.84,9.27C8.28,9.09 5.11,7.38 3,4.79C2.63,5.42 2.42,6.16 2.42,6.94C2.42,8.43 3.17,9.75 4.33,10.5C3.62,10.5 2.96,10.3 2.38,10C2.38,10 2.38,10 2.38,10.03C2.38,12.11 3.86,13.85 5.82,14.24C5.46,14.34 5.08,14.39 4.69,14.39C4.42,14.39 4.15,14.36 3.89,14.31C4.43,16 6,17.26 7.89,17.29C6.43,18.45 4.58,19.13 2.56,19.13C2.22,19.13 1.88,19.11 1.54,19.07C3.44,20.29 5.7,21 8.12,21C16,21 20.33,14.46 20.33,8.79C20.33,8.6 20.33,8.42 20.32,8.23C21.16,7.63 21.88,6.87 22.46,6Z" />
              </svg>
              <span className="text-gray-700 font-medium">Continue with Twitter</span>
            </button>
          </div>
          
          <div className="mt-6 text-center text-sm text-gray-500">
            <p>
              By continuing, you agree to our{' '}
              <a href="/terms" className="text-blue-600 hover:underline">Terms of Service</a>
              {' '}and{' '}
              <a href="/privacy" className="text-blue-600 hover:underline">Privacy Policy</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;