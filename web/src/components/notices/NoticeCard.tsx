import { useEffect, useState } from 'react';
import { Bell } from 'lucide-react';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';

interface Notice {
  id: string; // UUID from the API
  title: string;
  date: string;
  notice: string;
}

export const NoticeCard = () => {
  const [notices, setNotices] = useState<Notice[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedNotice, setSelectedNotice] = useState<Notice | null>(null);

  useEffect(() => {
    const fetchNotices = async () => {
      try {
        const response = await fetch('/api/v1/notice');
        if (!response.ok) {
          throw new Error(`Error: ${response.statusText}`);
        }
        const data: Notice[] = await response.json();
        setNotices(data);
      } catch (err) {
        setError('Failed to load notices.');
      } finally {
        setLoading(false);
      }
    };

    fetchNotices();
  }, []);

  return (
    <div className="max-w-md w-full p-6 bg-card shadow-md rounded-md">
      <h2 className="text-xl font-semibold mb-4 flex items-center gap-2 text-primary">
        <Bell className="h-6 w-6" />
        Latest Notices
      </h2>
      <div className="space-y-4">
        {loading && <p>Loading notices...</p>}
        {error && <p className="text-destructive">{error}</p>}
        {!loading &&
          !error &&
          notices.map((notice) => (
            <div
              key={notice.id}
              className="p-4 border border-border rounded-md cursor-pointer hover:bg-muted transition"
              onClick={() => setSelectedNotice(notice)}
            >
              <h3 className="text-base font-semibold">{notice.title}</h3>
              <p className="text-sm text-muted-foreground">{notice.date}</p>
            </div>
          ))}
      </div>

      {/* Dialog for displaying notice details */}
      <Dialog
        open={!!selectedNotice}
        onOpenChange={() => setSelectedNotice(null)}
      >
        <DialogContent>
          <DialogHeader>
            <DialogTitle className="text-lg font-bold text-primary-foreground">
              {selectedNotice?.title}
            </DialogTitle>
            <DialogDescription className="text-sm text-muted-foreground">
              {selectedNotice?.date}
            </DialogDescription>
          </DialogHeader>
          <p className="text-sm text-gray-800 mt-2">{selectedNotice?.notice}</p>
          <Button
            className="mt-4 bg-primary hover:bg-primary-dark"
            onClick={() => setSelectedNotice(null)}
          >
            Close
          </Button>
        </DialogContent>
      </Dialog>
    </div>
  );
};
