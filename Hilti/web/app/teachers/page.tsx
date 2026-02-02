
import { searchTeachers, getAllTeachers } from "@/database/queries";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import Link from "next/link";
import { Search } from "lucide-react";
import { redirect } from "next/navigation";

export default async function TeachersPage({
  searchParams,
}: {
  searchParams: Promise<{ q?: string }>;
}) {
  const { q } = await searchParams;
  
  // If no query, show all (or top 100)
  const teachers = q ? await searchTeachers(q) : await getAllTeachers();

  // Simple server action for search
  async function searchAction(formData: FormData) {
    "use server";
    const query = formData.get("q");
    if (typeof query === "string") {
      redirect(`/teachers?q=${encodeURIComponent(query)}`);
    }
  }

  return (
    <main className="container mx-auto py-10 px-4">
      <div className="mb-8 space-y-4">
        <h1 className="text-3xl font-bold">Find a Teacher</h1>
        <form action={searchAction} className="flex gap-2 max-w-lg">
          <Input 
            name="q" 
            placeholder="Search by code (e.g. GRU, MUE)..." 
            defaultValue={q}
            className="flex-1"
          />
          <Button type="submit">
            <Search className="mr-2 h-4 w-4" />
            Search
          </Button>
        </form>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
        {teachers.map((t) => (
          <Link href={`/teachers/${t.id}`} key={t.id} className="transition-transform hover:scale-105">
            <Card className="h-full hover:bg-muted/50">
              <CardHeader className="p-4 text-center">
                <div className="mx-auto w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mb-2 font-bold text-primary">
                  {t.code.substring(0, 2)}
                </div>
                <CardTitle className="text-lg">{t.code}</CardTitle>
                {/* Check if 'absences' exists on the object (it comes from searchTeachers but maybe not getAllTeachers) */}
                {(t as any).absences !== undefined && (
                   <CardDescription>{(t as any).absences} Absences</CardDescription>
                )}
              </CardHeader>
            </Card>
          </Link>
        ))}
        {teachers.length === 0 && (
          <div className="col-span-full text-center text-muted-foreground py-10">
            No teachers found matching &quot;{q}&quot;.
          </div>
        )}
      </div>
    </main>
  );
}
