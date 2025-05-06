import React, { useState, useEffect } from 'react';
import PostCard from '../components/posts/PostCard';
import { useAuth } from '../contexts/AuthContext';
import { Sparkles, TrendingUp } from 'lucide-react';

interface Post {
  id: string;
  userId: string;
  userName: string;
  userImage: string;
  content: string;
  images: string[];
  videoUrl?: string;
  createdAt: Date;
  likes: number;
  comments: number;
  isLiked: boolean;
}

const HomePage: React.FC = () => {
  const { currentUser } = useAuth();
  const [activeTab, setActiveTab] = useState('forYou');
  const [posts, setPosts] = useState<Post[]>([]);
  
  useEffect(() => {
    // Mock posts data
    const mockPosts: Post[] = [
      {
        id: '1',
        userId: '2',
        userName: 'Jane Smith',
        userImage: 'https://randomuser.me/api/portraits/women/57.jpg',
        content: 'Just finished this JavaScript tutorial series! Here are some key takeaways for beginners:  \n\n1. Start with understanding variables, data types, and functions \n2. Practice with small projects \n3. Learn ES6+ features \n4. Explore DOM manipulation \n\nWhat are your favorite JS learning resources?',
        images: ['https://images.pexels.com/photos/270348/pexels-photo-270348.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260'],
        createdAt: new Date(Date.now() - 3 * 3600000),
        likes: 24,
        comments: 5,
        isLiked: false
      },
      {
        id: '2',
        userId: '3',
        userName: 'Mike Johnson',
        userImage: 'https://randomuser.me/api/portraits/men/22.jpg',
        content: "Made these illustrations today while practicing digital art! I'm definitely seeing progress compared to last month. Still working on lighting and proportions. Any tips from experienced artists?",
        images: [
          'https://images.pexels.com/photos/1646953/pexels-photo-1646953.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260',
          'https://images.pexels.com/photos/3082341/pexels-photo-3082341.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260'
        ],
        createdAt: new Date(Date.now() - 12 * 3600000),
        likes: 56,
        comments: 8,
        isLiked: true
      },
      {
        id: '3',
        userId: '4',
        userName: 'Sarah Williams',
        userImage: 'https://randomuser.me/api/portraits/women/31.jpg',
        content: 'I just uploaded a quick tutorial on creating a responsive navbar with CSS Grid and Flexbox. Check it out and let me know what you think!',
        videoUrl: 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4',
        images: [],
        createdAt: new Date(Date.now() - 24 * 3600000),
        likes: 89,
        comments: 12,
        isLiked: false
      }
    ];
    
    setPosts(mockPosts);
  }, []);
  
  return (
    <div className="pb-10">
      {/* Welcome Section for Guest Users */}
      {!currentUser && (
        <div className="bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-xl p-6 mb-6 shadow-md">
          <h1 className="text-2xl md:text-3xl font-bold mb-2">Welcome to SkillShare</h1>
          <p className="text-white/80 mb-4">
            Join our community to share your skills, track your learning journey, and connect with other learners.
          </p>
          <div className="flex flex-wrap gap-3">
            <a 
              href="/login" 
              className="px-5 py-2 bg-white text-blue-600 font-medium rounded-lg hover:bg-blue-50 transition-colors"
            >
              Get Started
            </a>
            <a 
              href="/about" 
              className="px-5 py-2 bg-transparent border border-white text-white font-medium rounded-lg hover:bg-white/10 transition-colors"
            >
              Learn More
            </a>
          </div>
        </div>
      )}
      
      {/* Feed Tabs */}
      <div className="bg-white rounded-xl shadow-sm mb-6 overflow-hidden">
        <div className="flex border-b border-gray-200">
          <button 
            className={`flex-1 py-3 px-4 text-center font-medium text-sm ${
              activeTab === 'forYou' ? 'text-blue-600 border-b-2 border-blue-500' : 'text-gray-500 hover:text-gray-700'
            }`}
            onClick={() => setActiveTab('forYou')}
          >
            <div className="flex items-center justify-center">
              <Sparkles className="h-4 w-4 mr-1.5" />
              <span>For You</span>
            </div>
          </button>
          <button 
            className={`flex-1 py-3 px-4 text-center font-medium text-sm ${
              activeTab === 'trending' ? 'text-blue-600 border-b-2 border-blue-500' : 'text-gray-500 hover:text-gray-700'
            }`}
            onClick={() => setActiveTab('trending')}
          >
            <div className="flex items-center justify-center">
              <TrendingUp className="h-4 w-4 mr-1.5" />
              <span>Trending</span>
            </div>
          </button>
        </div>
      </div>
      
      {/* Feed Content */}
      <div className="space-y-4">
        {posts.length > 0 ? (
          posts.map(post => <PostCard key={post.id} post={post} />)
        ) : (
          <div className="text-center py-10">
            <p className="text-gray-500">No posts to display.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default HomePage;