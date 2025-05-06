import { useState, useEffect } from 'react';
import { Search, Plus, X } from 'lucide-react';
// Ensure the correct path to the ResourceCard component
import ResourceCard from '../components/ResourceCard';

type Resource = {
  id: string;
  title: string;
  description: string;
  url: string;
  resourceType: 'VIDEO' | 'ARTICLE' | 'BOOK' | 'TOOL';
  skillCategory: string;
  likes: number;
  createdAt: string;
  isOwner: boolean;
};

type ResourceFormData = {
  title: string;
  description: string;
  url: string;
  resourceType: 'VIDEO' | 'ARTICLE' | 'BOOK' | 'TOOL';
  skillCategory: string;
};

const ResourceLibraryPage = () => {
  const [resources, setResources] = useState<Resource[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedType, setSelectedType] = useState<string>('');
  const [selectedCategory, setSelectedCategory] = useState<string>('');
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState<ResourceFormData>({
    title: '',
    description: '',
    url: '',
    resourceType: 'ARTICLE',
    skillCategory: '',
  });
  const [editingId, setEditingId] = useState<string | null>(null);

  useEffect(() => {
    fetchResources();
  }, [searchQuery, selectedType, selectedCategory]);

  const fetchResources = async () => {
    try {
      setLoading(true);
      // Build the query string properly with template literals
      let url = '/api/resources?';
      if (searchQuery) url += `search=${searchQuery}&`;
      if (selectedType) url += `type=${selectedType}&`;
      if (selectedCategory) url += `category=${selectedCategory}`;
      
      // For development/testing - use mock data
      // In a real environment, you would use the actual API call
      // const response = await fetch(url);
      // const data = await response.json();
      
      // Mock data for demonstration
      setTimeout(() => {
        const mockResources: Resource[] = [
          {
            id: '1',
            title: 'React Hooks Complete Guide',
            description: 'Learn everything about React hooks with practical examples and best practices.',
            url: 'https://reactjs.org/docs/hooks-intro.html',
            resourceType: 'ARTICLE',
            skillCategory: 'Frontend',
            likes: 42,
            createdAt: '2025-03-15T10:30:00',
            isOwner: true
          },
          {
            id: '2',
            title: 'TypeScript for React Developers',
            description: 'A comprehensive course on using TypeScript with React to build type-safe applications.',
            url: 'https://www.typescript-course.com/react',
            resourceType: 'VIDEO',
            skillCategory: 'Frontend',
            likes: 28,
            createdAt: '2025-03-10T14:20:00',
            isOwner: false
          },
          {
            id: '3',
            title: 'Spring Boot REST API Design',
            description: 'Best practices for designing RESTful APIs with Spring Boot and Spring Data.',
            url: 'https://spring.io/guides/tutorials/rest/',
            resourceType: 'ARTICLE',
            skillCategory: 'Backend',
            likes: 35,
            createdAt: '2025-02-25T09:15:00',
            isOwner: true
          },
          {
            id: '4',
            title: 'Docker for Java Developers',
            description: 'Learn how to containerize your Java applications with Docker.',
            url: 'https://www.docker.com/java',
            resourceType: 'BOOK',
            skillCategory: 'DevOps',
            likes: 19,
            createdAt: '2025-03-05T11:45:00',
            isOwner: false
          }
        ];

        // Filter mock data based on search criteria
        let filteredResources = [...mockResources];
        
        if (searchQuery) {
          const query = searchQuery.toLowerCase();
          filteredResources = filteredResources.filter(
            resource => 
              resource.title.toLowerCase().includes(query) || 
              resource.description.toLowerCase().includes(query)
          );
        }
        
        if (selectedType) {
          filteredResources = filteredResources.filter(
            resource => resource.resourceType === selectedType
          );
        }
        
        if (selectedCategory) {
          filteredResources = filteredResources.filter(
            resource => resource.skillCategory === selectedCategory
          );
        }
        
        setResources(filteredResources);
        setLoading(false);
      }, 800); // Simulate network delay
    } catch (error) {
      console.error('Error fetching resources:', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const method = editingId ? 'PUT' : 'POST';
      const url = editingId 
        ? `/api/resources/${editingId}`
        : '/api/resources';

      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setShowForm(false);
        setFormData({
          title: '',
          description: '',
          url: '',
          resourceType: 'ARTICLE',
          skillCategory: '',
        });
        setEditingId(null);
        fetchResources();
      }
    } catch (error) {
      console.error('Error saving resource:', error);
    }
  };

  const handleEdit = (resource: Resource) => {
    setFormData({
      title: resource.title,
      description: resource.description,
      url: resource.url,
      resourceType: resource.resourceType,
      skillCategory: resource.skillCategory,
    });
    setEditingId(resource.id);
    setShowForm(true);
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Are you sure you want to delete this resource?')) {
      try {
        const response = await fetch(`/api/resources/${id}`, {
          method: 'DELETE',
        });

        if (response.ok) {
          fetchResources();
        }
      } catch (error) {
        console.error('Error deleting resource:', error);
      }
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">Resource Library</h1>
        <button
          onClick={() => setShowForm(true)}
          className="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          <Plus className="h-5 w-5 mr-2" />
          Add Resource
        </button>
      </div>

      <div className="flex flex-wrap gap-4 mb-6">
        <div className="flex-1 min-w-[300px]">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5" />
            <input
              type="text"
              placeholder="Search resources..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>
        
        <select
          value={selectedType}
          onChange={(e) => setSelectedType(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">All Types</option>
          <option value="VIDEO">Videos</option>
          <option value="ARTICLE">Articles</option>
          <option value="BOOK">Books</option>
          <option value="TOOL">Tools</option>
        </select>

        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">All Categories</option>
          <option value="Frontend">Frontend</option>
          <option value="Backend">Backend</option>
          <option value="DevOps">DevOps</option>
          <option value="Mobile">Mobile</option>
          <option value="UI/UX">UI/UX</option>
        </select>
      </div>

      {showForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-xl p-6 max-w-lg w-full">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold">
                {editingId ? 'Edit Resource' : 'Add New Resource'}
              </h2>
              <button
                onClick={() => {
                  setShowForm(false);
                  setEditingId(null);
                  setFormData({
                    title: '',
                    description: '',
                    url: '',
                    resourceType: 'ARTICLE',
                    skillCategory: '',
                  });
                }}
                className="text-gray-400 hover:text-gray-600"
              >
                <X className="h-5 w-5" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Title
                </label>
                <input
                  type="text"
                  required
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description
                </label>
                <textarea
                  required
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  rows={3}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  URL
                </label>
                <input
                  type="url"
                  required
                  value={formData.url}
                  onChange={(e) => setFormData({ ...formData, url: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Type
                  </label>
                  <select
                    required
                    value={formData.resourceType}
                    onChange={(e) => setFormData({ ...formData, resourceType: e.target.value as any })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="VIDEO">Video</option>
                    <option value="ARTICLE">Article</option>
                    <option value="BOOK">Book</option>
                    <option value="TOOL">Tool</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Category
                  </label>
                  <select
                    required
                    value={formData.skillCategory}
                    onChange={(e) => setFormData({ ...formData, skillCategory: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Select a category</option>
                    <option value="Frontend">Frontend</option>
                    <option value="Backend">Backend</option>
                    <option value="DevOps">DevOps</option>
                    <option value="Mobile">Mobile</option>
                    <option value="UI/UX">UI/UX</option>
                  </select>
                </div>
              </div>

              <div className="flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => {
                    setShowForm(false);
                    setEditingId(null);
                  }}
                  className="px-4 py-2 text-gray-600 hover:text-gray-800"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                >
                  {editingId ? 'Update' : 'Add'} Resource
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {resources.map((resource) => (
          <ResourceCard
            key={resource.id}
            resource={resource}
            onEdit={() => handleEdit(resource)}
            onDelete={() => handleDelete(resource.id)}
          />
        ))}
      </div>
    </div>
  );
};

export default ResourceLibraryPage;