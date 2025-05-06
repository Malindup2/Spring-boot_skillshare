import { ExternalLink, Edit, Trash2, ThumbsUp } from 'lucide-react';
import { format } from 'date-fns';

type ResourceType = 'VIDEO' | 'ARTICLE' | 'BOOK' | 'TOOL';

type Resource = {
  id: string;
  title: string;
  description: string;
  url: string;
  resourceType: ResourceType;
  skillCategory: string;
  likes: number;
  createdAt: string;
  isOwner: boolean;
};

type ResourceCardProps = {
  resource: Resource;
  onEdit: () => void;
  onDelete: () => void;
};

const ResourceCard = ({ resource, onEdit, onDelete }: ResourceCardProps) => {
  const getResourceTypeIcon = (type: ResourceType) => {
    switch (type) {
      case 'VIDEO':
        return 'bg-red-100 text-red-700';
      case 'ARTICLE':
        return 'bg-blue-100 text-blue-700';
      case 'BOOK':
        return 'bg-purple-100 text-purple-700';
      case 'TOOL':
        return 'bg-green-100 text-green-700';
      default:
        return 'bg-gray-100 text-gray-700';
    }
  };

  return (
    <div className="bg-white rounded-lg border border-gray-200 shadow-sm overflow-hidden hover:shadow-md transition-shadow">
      <div className="p-5">
        <div className="flex justify-between items-start mb-2">
          <h3 className="text-lg font-semibold text-gray-900 mb-1 flex-1">{resource.title}</h3>
          <span className={`px-2 py-1 text-xs font-medium rounded-full ${getResourceTypeIcon(resource.resourceType)}`}>
            {resource.resourceType.charAt(0) + resource.resourceType.slice(1).toLowerCase()}
          </span>
        </div>
        
        <p className="text-gray-600 text-sm mb-3 line-clamp-2">{resource.description}</p>
        
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <a 
              href={resource.url} 
              target="_blank"
              rel="noopener noreferrer" 
              className="inline-flex items-center text-sm font-medium text-blue-600 hover:text-blue-800"
            >
              <ExternalLink className="h-4 w-4 mr-1" />
              Visit
            </a>
            <span className="inline-flex items-center text-sm text-gray-500">
              <ThumbsUp className="h-4 w-4 mr-1" />
              {resource.likes}
            </span>
          </div>
          
          {resource.isOwner && (
            <div className="flex items-center space-x-2">
              <button 
                onClick={onEdit}
                className="p-1 text-gray-400 hover:text-blue-500"
                aria-label="Edit"
              >
                <Edit className="h-4 w-4" />
              </button>
              <button 
                onClick={onDelete}
                className="p-1 text-gray-400 hover:text-red-500"
                aria-label="Delete"
              >
                <Trash2 className="h-4 w-4" />
              </button>
            </div>
          )}
        </div>
      </div>
      
      <div className="bg-gray-50 px-5 py-2 text-xs text-gray-500 flex justify-between items-center">
        <span className="font-medium bg-gray-200 rounded-full px-2 py-0.5">
          {resource.skillCategory}
        </span>
        {resource.createdAt && (
          <span>Added {format(new Date(resource.createdAt), 'MMM d, yyyy')}</span>
        )}
      </div>
    </div>
  );
};

export default ResourceCard;