
import { getCancelledLessons } from "@/database/queries";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";

export const revalidate = 60; 

export default async function CancellationsPage() {
  const cancellations = await getCancelledLessons(100);

  // Helper to format date
  const formatDate = (dateInt: number | null) => {
    if (!dateInt) return "N/A";
    const s = dateInt.toString();
    return `${s.slice(6, 8)}.${s.slice(4, 6)}.${s.slice(0, 4)}`;
  };

  return (
    <main className="container mx-auto py-10 px-4">
      <div className="mb-8 space-y-2">
        <h1 className="text-3xl font-bold">Dismissed Lessons</h1>
        <p className="text-muted-foreground">Showing the latest 100 cancelled lessons.</p>
      </div>

      <Card>
        <CardContent className="p-0">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Date</TableHead>
                <TableHead>Period</TableHead>
                <TableHead>Class</TableHead>
                <TableHead>Subject</TableHead>
                <TableHead>Teacher</TableHead>
                <TableHead>Room</TableHead>
                <TableHead>Info</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {cancellations.map((entry) => (
                <TableRow key={entry.id}>
                  <TableCell className="font-medium whitespace-nowrap">{formatDate(entry.date)}</TableCell>
                  <TableCell>{entry.period}</TableCell>
                   <TableCell>{entry.group || "-"}</TableCell>
                  <TableCell>
                    <Badge variant="outline">{entry.subject || "N/A"}</Badge>
                  </TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                       <Avatar className="h-6 w-6">
                        <AvatarFallback className="text-[10px]">{entry.teacher?.substring(0, 2)}</AvatarFallback>
                      </Avatar>
                      {entry.teacher}
                    </div>
                  </TableCell>
                  <TableCell className="text-muted-foreground">{entry.room || "-"}</TableCell>
                  <TableCell>
                     {/* info_flag text often contains 'Entfall' or similar */}
                    <span className="text-destructive font-medium">{entry.info}</span>
                  </TableCell>
                </TableRow>
              ))}
              {cancellations.length === 0 && (
                <TableRow>
                  <TableCell colSpan={7} className="text-center py-10 text-muted-foreground">
                    No cancelled lessons found.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </main>
  );
}
